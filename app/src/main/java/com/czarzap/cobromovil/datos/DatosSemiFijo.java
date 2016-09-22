package com.czarzap.cobromovil.datos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.pagos.PagosSemiFijo;
import com.czarzap.cobromovil.service.DatosComercioService;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.dd.CircularProgressButton;

import java.math.BigDecimal;

import driver.Contants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatosSemiFijo extends Activity{

    private DatosComercioService service;
    private TextView tvLocal;
    private EditText etPropietario;
    private EditText etDomicilio,etDomicilioNotif;
    private EditText etColonia;
    private EditText etFrente;
    private EditText etFondo;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private EditText etRuta;
    private CircularProgressButton bSiguiente;
    private Switch sStatus;
    private InComercios comercio;
    private Integer control,empresa;
    private String tipo,ruta;
    private BigDecimal tarifa;
    private LinearLayout linearLayout;
    int onStartCount = 0;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_datos_comercio);
        linearLayout = (LinearLayout) findViewById(R.id.Linear) ;
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
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

    private void initViews(){
        DatabaseManager manager = new DatabaseManager(this);
        progress=(ProgressBar)findViewById(R.id.progress_bar);
        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etDomicilioNotif = (EditText) findViewById(R.id.domicilioNotificaciones);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etFrente = (EditText) findViewById(R.id.etFrente);
        etFondo = (EditText) findViewById(R.id.etFondo);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        etRuta = (EditText) findViewById(R.id.etRutaSemi);
        etFrente.setVisibility(View.VISIBLE);
        etFondo.setVisibility(View.VISIBLE);
        etRuta.setVisibility(View.VISIBLE);
        bSiguiente   = (CircularProgressButton) findViewById(R.id.bSiguiente);
        sStatus = (Switch) findViewById(R.id.sStatus);
        comercio = new InComercios();
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
            bSiguiente.setEnabled(false);
        }

        control = Integer.valueOf(getIntent().getExtras().getString("control"));                 // Leer los datos pasados por el QR
        tipo = getIntent().getExtras().getString("tipo");
        empresa = Integer.valueOf(getIntent().getExtras().getString("empresa"));
        ruta = getIntent().getExtras().getString("ruta");

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
                if(comercio.getCom_local() != null){
                    if (comercio.getCom_local().equals("S")) tvLocal.setText("LOCAL");
                    else tvLocal.setText("EXTERNO");
                }
                else tvLocal.setVisibility(View.GONE);
                if (comercio.getCom_nombre_propietario() != null)     etPropietario.setText(comercio.getCom_nombre_propietario());
                if (comercio.getCom_domicilio() != null)             etDomicilio.setText(comercio.getCom_domicilio());
                if (comercio.getCom_domicilio_notificaciones() != null)             etDomicilioNotif.setText(comercio.getCom_domicilio_notificaciones());
                if (comercio.getCom_colonia() != null)           etColonia.setText(comercio.getCom_colonia());
                if (comercio.getCom_frente() != null)            etFrente.setText(String.valueOf(comercio.getCom_frente().toString()));
                if (comercio.getCom_fondo() != null)             etFondo.setText(String.valueOf(comercio.getCom_fondo()));
                if (comercio.getCom_ocupante() != null)     etQuien.setText(comercio.getCom_ocupante());
                if (comercio.getCom_control() != null)           etControl.setText(comercio.getCom_control().toString());
                if (comercio.getCom_ult_eje() != null)  etLicencia.setText(comercio.getCom_ult_eje().toString());
                if (comercio.getCom_ruta() != null)              etRuta.setText(comercio.getCom_ruta());
                if(comercio.getCom_status() != null){
                    if (comercio.getCom_status().equals("A"))        sStatus.setChecked(true);
                    else sStatus.setChecked(false);
                }
                progress.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                bSiguiente.setVisibility(View.VISIBLE);
                Call<BigDecimal> call_ = service.getTarifa(empresa,tipo,comercio.getCom_local());
                call_.enqueue(new Callback<BigDecimal>() {
                    @Override
                    public void onResponse(Call<BigDecimal> call, Response<BigDecimal> response) {
                        if(response.body() != null) tarifa = response.body();

                    }

                    @Override
                    public void onFailure(Call<BigDecimal> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "No se encontro Tarifa", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(getApplicationContext(), "Error Comercio, revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        });

        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(sStatus.isChecked()) {
                        Bundle args = new Bundle();
                        args.putInt("empresa", comercio.getCom_empresa());
                        args.putInt("control", comercio.getCom_control());
                        args.putInt("contribuyente", comercio.getCom_contribuyente());
                        args.putString("tipo", comercio.getCom_tipo());
                        args.putString("propietario", comercio.getCom_nombre_propietario());
                        args.putString("domicilio", comercio.getCom_domicilio_notificaciones() + " " + comercio.getCom_colonia());
                        args.putString("quien", comercio.getCom_ocupante());
                        args.putString("ruta", ruta);
                        args.putString("rutaID", comercio.getCom_ruta());
                        if (tarifa.equals(null)) tarifa = BigDecimal.valueOf(0d);
                        args.putDouble("tarifa", tarifa.doubleValue());
                        Intent comercioIntent = new Intent(DatosSemiFijo.this, PagosSemiFijo.class);
                        comercioIntent.putExtras(args);
                        DatosSemiFijo.this.startActivity(comercioIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "El comercio debe de estar Activo", Toast.LENGTH_LONG).show();
                    }
                }
        });
    }



    public Boolean required(EditText editText){
        String  str = editText.getText().toString();
        if(str.equalsIgnoreCase(""))
        {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }
}
