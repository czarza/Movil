package com.czarzap.cobromovil.pagos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.service.DatosComercioService;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;

import java.math.BigDecimal;

import driver.Contants;
import driver.HsBluetoothPrintDriver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VistaPagoSemiFijo extends Activity {
    int onStartCount = 0;
    private InComercio_cobro_movil comercio_cobro_movil;
    private DatosComercioService service;
    private TextView tvLocal;
    private EditText etPropietario,etPago,etFecha,etComercio;
    private EditText etDomicilio,etDomicilioNotif;
    private EditText etColonia;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private EditText etRuta;
    private CircularProgressButton bSiguiente;
    private Switch sStatus;
    private InComercios comercio;
    private String tipo,ruta;
    private LinearLayout linearLayout;
    private String nombreEmpresa,domicilioEmpresa,rfcEmpresa;
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
    DatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pago_semi_fijo);
        manager = new DatabaseManager(this);
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



    private void initViews(){
        linearLayout = (LinearLayout) findViewById(R.id.Linear) ;
        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etDomicilioNotif = (EditText) findViewById(R.id.domicilioNotificaciones);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        etRuta = (EditText) findViewById(R.id.etRuta);
        etPago = (EditText) findViewById(R.id.etPago);
        etFecha = (EditText) findViewById(R.id.etFecha);
        bSiguiente   = (CircularProgressButton) findViewById(R.id.bSiguiente);
        etComercio = (EditText) findViewById(R.id.etComercio);
        comercio = new InComercios();
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
            bSiguiente.setEnabled(false);
        }
         loadActivity();
    }


    private void loadActivity(){

        Intent i = getIntent();
        comercio_cobro_movil = (InComercio_cobro_movil)i.getSerializableExtra("comercio");
        Integer empresa = comercio_cobro_movil.getCac_empresa();
        Integer control = comercio_cobro_movil.getCac_control();
        String tipo = comercio_cobro_movil.getCac_tipo();
        nombreEmpresa = manager.getNombreEmpresa();
        domicilioEmpresa = manager.getDomicilioEmpresa();
        rfcEmpresa = manager.getRfceEmpresa();

        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DatosComercioService.class);  // El servicio del Datos Comercio

        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
            bSiguiente.setEnabled(false);
        }

        Call<InComercios> call = service.getComercio(empresa,control,tipo);
        call.enqueue(new Callback<InComercios>() {
            @Override
            public void onResponse(Call<InComercios> call, Response<InComercios> response) {
                comercio = response.body();
                if(comercio.getCom_local() != null){
                    if (comercio.getCom_local().equals("S")) tvLocal.setText("LOCAL");
                    else tvLocal.setText("EXTERNO");
                }
                else tvLocal.setVisibility(View.GONE);
                if (comercio.getCom_nombre_propietario() != null)       etPropietario.setText(comercio.getCom_nombre_propietario());
                if (comercio.getCom_domicilio() != null)                etDomicilio.setText(comercio.getCom_domicilio());
                if (comercio.getCom_domicilio_notificaciones() != null) etDomicilioNotif.setText(comercio.getCom_domicilio_notificaciones());
                if (comercio.getCom_colonia() != null)                  etColonia.setText(comercio.getCom_colonia());
                if (comercio.getCom_ocupante() != null)                 etQuien.setText(comercio.getCom_ocupante());
                if (comercio.getCom_control() != null)                  etControl.setText(comercio.getCom_control().toString());
                if (comercio.getCom_ult_eje() != null)                  etLicencia.setText(comercio.getCom_ult_eje().toString());
                if (comercio.getCom_ruta() != null)                     etRuta.setText(comercio.getCom_ruta());
                etPago.setText(comercio_cobro_movil.getCac_total().toString());
                etFecha.setText(comercio_cobro_movil.getFecha_pago());
                etComercio.setText(tipoComercio(comercio.getCom_tipo()));
                linearLayout.setVisibility(View.VISIBLE);
                bSiguiente.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                bSiguiente.setEnabled(false);
            }
        });

        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util util = new Util();
                String agente = manager.getNombreAgente();       // obtener el nombre del Agente
                util.pago(comercio_cobro_movil,getApplicationContext(),hsBluetoothPrintDriver,
                        nombreEmpresa,domicilioEmpresa,rfcEmpresa,agente,ruta,
                        comercio.getCom_ocupante(),comercio.getCom_contribuyente(),comercio.getCom_nombre_propietario());
            }
        });

    }

    public String tipoComercio(String tipo){
        String campo = null;
        if (tipo.equals("F")) {
            campo = "Fijo";

        } else if (tipo.equals("S")) {
            campo = "Semi-Fijo";

        } else if (tipo.equals("A")) {
            campo = "Ambulante";

        }
        return campo;
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


}
