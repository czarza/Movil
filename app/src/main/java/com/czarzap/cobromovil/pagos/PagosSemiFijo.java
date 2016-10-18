package com.czarzap.cobromovil.pagos;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.menu.MenuActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import driver.HsBluetoothPrintDriver;

public class PagosSemiFijo extends BaseActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    int onStartCount = 0;
    private String nombreEmpresa,domicilioEmpresa,rfcEmpresa;
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
    private EditText etCosto;
    private CircularProgressButton bPagar, bImprimir,bRegresar;
    private EditText etNotas;
    private DatabaseManager manager;
    private InComercios comercio;
    private InComercio_cobro_movil pagos;
    Util util= new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costo_comercio);
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        hsBluetoothPrintDriver.Begin();
        manager = new DatabaseManager(this);  // llamar a la Base

        initViews();

    }

    private void initViews() {
        comercio = (InComercios) getIntent().getExtras().get("comercio");

        etCosto = (EditText) findViewById(R.id.etCosto);
        etNotas = (EditText) findViewById(R.id.etNotas);
        bPagar = (CircularProgressButton) findViewById(R.id.circularButton1);
        bImprimir = (CircularProgressButton) findViewById(R.id.bImprimir);
        bRegresar = (CircularProgressButton) findViewById(R.id.bRegresar);
        if (comercio.getTarifa() != null){
            etCosto.setText(String.valueOf(comercio.getTarifa()));
            etCosto.setEnabled(true);
        }

        nombreEmpresa = manager.getNombreEmpresa();
        domicilioEmpresa = manager.getDomicilioEmpresa();
        rfcEmpresa = manager.getRfceEmpresa();

        loadActivity();
    }

    private void loadActivity() {

        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        // Get local Bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no esta Disponible", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bPagar.setIndeterminateProgressMode(true);
        bPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double costo = Double.valueOf(etCosto.getText().toString());
                Double tarifa;
                if(comercio.getTarifa() == null) tarifa = 0d;
                else tarifa = comercio.getTarifa().doubleValue();
                if (required(etCosto)&&costo >= tarifa) {
                    String notas = etNotas.getText().toString();
                    if (notas.equalsIgnoreCase("")) notas = "";
                        try {
                            Offline(comercio,notas,costo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                else {
                    bPagar.setProgress(-1);
                    Toast.makeText(getApplicationContext(), "No puede ser menor a la tarifa", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            bPagar.setProgress(0);
                        }
                    }, 500);

                }
            }
        });

        bImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        bRegresar.setVisibility(View.VISIBLE);
                        String agente = manager.getNombreAgente();       // obtener el nombre del Agente
                        util.pago(pagos,getApplicationContext(),hsBluetoothPrintDriver,
                                nombreEmpresa,domicilioEmpresa,rfcEmpresa,agente,comercio);

            }
        });

        bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(PagosSemiFijo.this, MenuActivity.class);
                PagosSemiFijo.this.startActivity(comercioIntent);
            }
        });
    }

    private void Offline(InComercios comercio,String notas,Double costo) throws IOException {
        Log.d("Offline",comercio.toString());
        Integer empresa = comercio.getCom_empresa();
        Integer control = comercio.getCom_control();
        String tipo = comercio.getCom_tipo();
        Integer agente = manager.getAgente();
        Integer numeroPago = manager.getNextFolio(agente);
        pagos = new InComercio_cobro_movil(empresa,tipo,control,numeroPago);
        pagos.setCac_agente(agente.toString());
        pagos.setCac_notas(notas);
        pagos.setCac_total(new BigDecimal(costo));
        pagos.setPropietario(comercio.getCom_nombre_propietario());
        pagos.setRuta(comercio.getCom_ruta());

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        pagos.setCac_fecha_pago(format.format(new Date()));
        pagos.setFecha_pago(format.format(new Date()));
        Log.d("cac fecha", pagos.getCac_fecha_pago());
        Log.d("fecha", pagos.getFecha_pago());

        OfflineUtil offlineUtil = new OfflineUtil();
        List<InComercio_cobro_movil> p = new ArrayList<>();
        if(offlineUtil.fileExistPagos(getApplicationContext())){
            p = offlineUtil.pagosData(getApplicationContext());
            p.add(pagos);
        }
        else{
            p.add(pagos);
        }
        offlineUtil.pagosSave(p,getApplicationContext());
        bPagar.setProgress(100);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {bImprimir.setVisibility(CircularProgressButton.VISIBLE);}
        }, 800);
    }
    public Boolean required(EditText editText) {
        String str = editText.getText().toString();
        if (str.equalsIgnoreCase("")) {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }

}

