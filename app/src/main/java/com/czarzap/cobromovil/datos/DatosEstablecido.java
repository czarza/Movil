package com.czarzap.cobromovil.datos;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.service.DatosComercioService;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;

import driver.Contants;
import driver.HsBluetoothPrintDriver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatosEstablecido  extends Activity {

    private DatosComercioService service;
    private EditText etPropietario;
    private EditText etDomicilio;
    private EditText etColonia;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private CircularProgressButton bSiguiente,bEstado;
    private Switch sStatus;
    private InComercios comercio;
    private Integer control;
    private Integer empresa;
    private String tipo;
    private LinearLayout linearLayout;
    private Bundle args;
    private AlertDialog.Builder builder;
    private Util util = new Util();
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_datos_establecido);
        linearLayout = (LinearLayout) findViewById(R.id.LinearEstablecido) ;
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

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }

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
        control = Integer.valueOf(getIntent().getExtras().getString("control"));                 // Leer los datos pasados por el QR
        tipo = getIntent().getExtras().getString("tipo");
        empresa = Integer.valueOf(getIntent().getExtras().getString("empresa"));

        builder = new AlertDialog.Builder(this);
        DatabaseManager manager = new DatabaseManager(this);
        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        bEstado   = (CircularProgressButton) findViewById(R.id.bEstado);
        bSiguiente = (CircularProgressButton) findViewById(R.id.bSiguiente);
        sStatus = (Switch) findViewById(R.id.sStatus);
        comercio = new InComercios();
        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
            bEstado.setEnabled(false);
        }
        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DatosComercioService.class);  // El servicio del Datos Comercio

        loadActivity();
    }
    private void loadActivity(){
        Call<InComercios> call = service.getComercio(empresa,control,tipo);
        call.enqueue(new Callback<InComercios>() {
            @Override
            public void onResponse(Call<InComercios> call, Response<InComercios> response) {
                comercio = response.body();  // Obtener Clase
                if(comercio.getCom_contribuyente() != null){
                    initComercio();
                }
                else{

                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setMessage("No existe el Comercio Establecido").setTitle("ERROR").setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent comercioIntent = new Intent(DatosEstablecido.this, MenuActivity.class);
                            DatosEstablecido.this.startActivity(comercioIntent);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                Log.d("Comercio",t.getMessage());
                Toast.makeText(getApplicationContext(), "Error Comercio, revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initComercio(){
        if (comercio.getCom_nombre_propietario() != null)            etPropietario.setText(comercio.getCom_nombre_propietario());
        if (comercio.getCom_domicilio_notificaciones() != null)      etDomicilio.setText(comercio.getCom_domicilio_notificaciones());
        if (comercio.getCom_colonia() != null)                       etColonia.setText(comercio.getCom_colonia());
        if (comercio.getCom_ocupante() != null)                      etQuien.setText(comercio.getCom_ocupante());
        if (comercio.getCom_control() != null)                       etControl.setText(comercio.getCom_control().toString());
        if (comercio.getCom_ult_eje() != null)                       etLicencia.setText(comercio.getCom_ult_eje().toString());
        if (comercio.getCom_status().equals("A"))                    sStatus.setChecked(true);
        else sStatus.setChecked(false);

        linearLayout.setVisibility(View.VISIBLE);
            bSiguiente.setVisibility(View.GONE);
            bEstado.setVisibility(View.VISIBLE);
            bEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendDatatoCobro();
                    util.printEstadoCuenta(comercio,getApplicationContext(),hsBluetoothPrintDriver);
                }
            });
    }

    private void sendDatatoCobro(){

        comercio.setCom_nombre_propietario(etPropietario.getText().toString());
        comercio.setCom_domicilio_notificaciones(etDomicilio.getText().toString());
        comercio.setCom_colonia(etColonia.getText().toString());
        comercio.setCom_ocupante(etQuien.getText().toString());
        comercio.setCom_status("A");

        Call<InComercios> call = service.setComercio(comercio);
        call.enqueue(new Callback<InComercios>() {
            @Override
            public void onResponse(Call<InComercios> call, Response<InComercios> response) {
                InComercios fijo = response.body ();
                args = new Bundle();
                args.putInt("empresa",fijo.getCom_empresa());
                args.putInt("control",fijo.getCom_control());
                args.putInt("contribuyente",fijo.getCom_contribuyente());
                args.putString("tipo",fijo.getCom_tipo());
                args.putString ( "propietario",fijo.getCom_nombre_propietario ());
                args.putString("domicilio", fijo.getCom_domicilio_notificaciones () +" "+ fijo.getCom_colonia ());
                args.putString ( "quien", fijo.getCom_ocupante ());

            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        });

    }

}