package com.czarzap.cobromovil.datos;

import android.app.Activity;
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
    private EditText etPropietario;
    private EditText etDomicilio;
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
    private Integer tarifa;
    private LinearLayout linearLayout;
    int onStartCount = 0;

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

        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etFrente = (EditText) findViewById(R.id.etFrente);
        etFondo = (EditText) findViewById(R.id.etFondo);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        etRuta = (EditText) findViewById(R.id.etRuta);
        etFrente.setVisibility(View.VISIBLE);
        etFondo.setVisibility(View.VISIBLE);
        etRuta.setVisibility(View.VISIBLE);
        bSiguiente   = (CircularProgressButton) findViewById(R.id.bSiguiente);
        sStatus = (Switch) findViewById(R.id.sStatus);
        comercio = new InComercios();

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
                if (comercio.getCom_nombre_propietario() != null)     etPropietario.setText(comercio.getCom_nombre_propietario());
                if (comercio.getCom_domicilio_notificaciones() != null)             etDomicilio.setText(comercio.getCom_domicilio_notificaciones());
                if (comercio.getCom_colonia() != null)           etColonia.setText(comercio.getCom_colonia());
                if (comercio.getCom_frente() != null)            etFrente.setText(String.valueOf(comercio.getCom_frente().toString()));
                if (comercio.getCom_fondo() != null)             etFondo.setText(String.valueOf(comercio.getCom_fondo()));
                if (comercio.getCom_ocupante() != null)     etQuien.setText(comercio.getCom_ocupante());
                if (comercio.getCom_control() != null)           etControl.setText(comercio.getCom_control().toString());
                if (comercio.getCom_ult_eje() != null)  etLicencia.setText(comercio.getCom_ult_eje().toString());
                if (comercio.getCom_ruta() != null)              etRuta.setText(comercio.getCom_ruta());
                if (comercio.getCom_status().equals("A"))        sStatus.setChecked(true);
                else sStatus.setChecked(false);
                linearLayout.setVisibility(View.VISIBLE);
                Call<Integer> call_ = service.getTarifa(empresa,tipo,comercio.getCom_local());
                call_.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        tarifa = response.body();
                        bSiguiente.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error Tarifa, revise su conexion a Internet", Toast.LENGTH_LONG).show();
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
                if(required(etPropietario)&&required(etDomicilio)&&required(etColonia)&&required(etFrente)&&required(etFondo)&&required(etQuien)) {
                    if(sStatus.isChecked()){

                        comercio.setCom_nombre_propietario(etPropietario.getText().toString());
                        comercio.setCom_domicilio_notificaciones(etDomicilio.getText().toString());
                        comercio.setCom_colonia(etColonia.getText().toString());
                        comercio.setCom_frente(new BigDecimal(etFrente.getText().toString()));
                        comercio.setCom_fondo(new BigDecimal(etFondo.getText().toString()));
                        comercio.setCom_ocupante(etQuien.getText().toString());
                        comercio.setCom_status("A");


                        Call<InComercios> call = service.setComercio(comercio);
                        call.enqueue(new Callback<InComercios>() {
                            @Override
                            public void onResponse(Call<InComercios> call, Response<InComercios> response) {
                                InComercios fijo = response.body ();
                                Bundle args = new Bundle();
                                args.putInt("empresa",fijo.getCom_empresa());
                                args.putInt("control",fijo.getCom_control());
                                args.putInt("contribuyente",fijo.getCom_contribuyente());
                                args.putString("tipo",fijo.getCom_tipo());
                                args.putString ( "propietario",fijo.getCom_nombre_propietario ());
                                args.putString("domicilio", fijo.getCom_domicilio_notificaciones () +" "+ fijo.getCom_colonia ());
                                args.putString ( "quien", fijo.getCom_ocupante ());
                                args.putString ( "ruta", ruta);
                                args.putString ( "rutaID", fijo.getCom_ruta());
                                if(tarifa==null){ tarifa = 0;}
                                args.putInt ( "tarifa", tarifa);
                                Intent comercioIntent = new Intent(DatosSemiFijo.this, PagosSemiFijo.class);
                                comercioIntent.putExtras(args);
                                DatosSemiFijo.this.startActivity(comercioIntent);

                            }

                            @Override
                            public void onFailure(Call<InComercios> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "El comercio debe de estar Activo", Toast.LENGTH_LONG).show();
                    }

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
