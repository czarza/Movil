package com.czarzap.cobromovil.datos;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.comercio.VerImagenActivity;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.service.DatosComercioService;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;

import driver.HsBluetoothPrintDriver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatosMotos extends BaseActivity {
    private DatosComercioService service;
    private EditText etPropietario;
    private EditText etDomicilio;
    private EditText etColonia;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private EditText etPlaca;
    private EditText etCirculacion;
    private CircularProgressButton bEstado,bImagen;
    private Switch sStatus;
    private InComercios comercio;
    private Integer control;
    private Integer empresa;
    private String tipo;
    private LinearLayout linearLayout;
    private AlertDialog.Builder builder;
    private Boolean existeImagen;
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_datos_motos);
        linearLayout = (LinearLayout) findViewById(R.id.LinearMotos) ;
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        startPrinter();
        initViews();
    }

    private void startPrinter(){
        hsBluetoothPrintDriver.Begin();
        // Get local Bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no esta Disponible", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews(){
        builder = new AlertDialog.Builder(this);

        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        etCirculacion   = (EditText) findViewById(R.id.etCirculacion);
        etPlaca = (EditText) findViewById(R.id.etPlaca);
        bEstado   = (CircularProgressButton) findViewById(R.id.bEstado);
        bImagen   = (CircularProgressButton) findViewById(R.id.bImagen);
        sStatus = (Switch) findViewById(R.id.sStatus);
        comercio = new InComercios();

        comercio = (InComercios) getIntent().getExtras().get("comercio");
        if(comercio == null){
            Integer control = Integer.valueOf(getIntent().getExtras().getString("control"));
            String tipo = getIntent().getExtras().getString("tipo");

            OfflineUtil util = new OfflineUtil();
            if(util.fileExistsComercio(getApplicationContext())){
                comercio = util.getComercioOffline(getApplicationContext(),control,tipo);
                if(comercio == null){                   // El QR no corresponde al Sistema
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setMessage("No existe el Coemrcio").setTitle("ERROR").setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent menuIntent = new Intent(DatosMotos.this,MenuActivity.class);
                            DatosMotos.this.startActivity(menuIntent);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else initComercio();
            }
        }
        else{
            initComercio();
        }




    }

    private void initComercio(){
        loadImage();
        if (comercio.getCom_nombre_propietario() != null)            etPropietario.setText(comercio.getCom_nombre_propietario());
        if (comercio.getCom_domicilio_notificaciones() != null)      etDomicilio.setText(comercio.getCom_domicilio_notificaciones());
        if (comercio.getCom_ocupante() != null)                      etQuien.setText(comercio.getCom_ocupante());
        if (comercio.getCom_control() != null)                       etControl.setText(comercio.getCom_control().toString());
        if (comercio.getCom_ult_eje() != null)                       etLicencia.setText(comercio.getCom_ult_eje().toString());
        if (comercio.getCom_num_placa() != null)                     etPlaca.setText(comercio.getCom_num_placa());
        if (comercio.getCom_num_tarj_circulacion() != null)          etCirculacion.setText(comercio.getCom_num_tarj_circulacion());
        if (comercio.getCom_status().equals("A"))                    sStatus.setChecked(true);
        else sStatus.setChecked(false);

        linearLayout.setVisibility(View.VISIBLE);
        bEstado.setVisibility(View.VISIBLE);
        bEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util util = new Util();
                util.printEstadoCuentaMoto(comercio,getApplicationContext(),hsBluetoothPrintDriver);
            }
        });
    }


    private void loadImage(){
        DatabaseManager manager = new DatabaseManager(this);
        control = Integer.valueOf(getIntent().getExtras().getString("control"));                 // Leer los datos pasados por el QR
        tipo = getIntent().getExtras().getString("tipo");
        empresa = Integer.valueOf(getIntent().getExtras().getString("empresa"));

        String url = manager.getWebService(1);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        service = retrofit.create(DatosComercioService.class);

        Call<Boolean> call = service.existeImagen(empresa,control,tipo);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                existeImagen=response.body();
                if(existeImagen){
                    bImagen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                bImagen.setVisibility(View.GONE);
            }
        });

        bImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("empresa", String.valueOf(empresa));
                args.putString("control", String.valueOf(control));
                args.putString("tipo",tipo);
                Intent rutasIntent = new Intent(DatosMotos.this,VerImagenActivity.class);
                rutasIntent.putExtras ( args );
                DatosMotos.this.startActivity(rutasIntent);
            }
        });
    }

}
