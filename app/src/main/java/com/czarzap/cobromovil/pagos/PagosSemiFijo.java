package com.czarzap.cobromovil.pagos;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.service.CostoComercioService;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import driver.Contants;
import driver.HsBluetoothPrintDriver;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PagosSemiFijo extends Activity {

    private static final int REQUEST_ENABLE_BT = 2;
    int onStartCount = 0;
    private final int handlerSign = 7173;

    Integer empresa, control, tarifa, contribuyente;
    String tipo, propietario, quienOcupa, ruta, nombreEmpresa, domicilioEmpresa, rfcEmpresa, rutaID;
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
    private EditText etCosto;
    private CircularProgressButton bPagar, bImprimir,bRegresar;
    private EditText etNotas;
    private DatabaseManager manager;
    private InComercio_cobro_movil pagos;
    private CostoComercioService service;
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

    private void initViews() {

        empresa = getIntent().getExtras().getInt("empresa");
        control = getIntent().getExtras().getInt("control");                 // Leer los datos pasados por el QR
        contribuyente = getIntent().getExtras().getInt("contribuyente");
        tipo = getIntent().getExtras().getString("tipo");
        propietario = getIntent().getExtras().getString("propietario");
        quienOcupa = getIntent().getExtras().getString("quien");
        ruta = getIntent().getExtras().getString("ruta");
        rutaID = getIntent().getExtras().getString("rutaID");
        tarifa = getIntent().getExtras().getInt("tarifa");
        etCosto = (EditText) findViewById(R.id.etCosto);
        etNotas = (EditText) findViewById(R.id.etNotas);
        bPagar = (CircularProgressButton) findViewById(R.id.circularButton1);
        bImprimir = (CircularProgressButton) findViewById(R.id.bImprimir);
        bRegresar = (CircularProgressButton) findViewById(R.id.bRegresar);

        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
            bPagar.setEnabled(false);
        }

        if (tarifa != 0){
            etCosto.setText(tarifa.toString());
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

        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CostoComercioService.class);  // El servicio del Datos Comercio

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
                bPagar.setProgress(50);        // Cargando
                Double costo = Double.valueOf(etCosto.getText().toString());
                if (required(etCosto)&&costo >= tarifa) {

                    String notas = etNotas.getText().toString();
                    if (notas.equalsIgnoreCase("")) notas = "";
                    Integer agente = manager.getAgente();    // Obtener el numero de agente

                    Call<InComercio_cobro_movil> call = service.setPago(empresa, control, contribuyente,tipo, new BigDecimal(costo), agente, notas, rutaID);  // hace la llamada hacia SifiReceptoria

                    call.enqueue(new Callback<InComercio_cobro_movil>() {
                        @Override
                        public void onResponse(Call<InComercio_cobro_movil> call, Response<InComercio_cobro_movil> response) {
                            pagos = response.body();
                            if (pagos.getCac_total() == null) {
                                bPagar.setProgress(-1);
                                Toast.makeText(getApplicationContext(), "No esta autorizado para cobrar este Comercio", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        bPagar.setProgress(-1);
                                    }
                                }, 500);
                            } else {
                                bPagar.setProgress(100);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {bImprimir.setVisibility(CircularProgressButton.VISIBLE);}
                                }, 800);
                            }
                        }
                        @Override
                        public void onFailure(Call<InComercio_cobro_movil> call, Throwable t) {Log.d("Error", t.getMessage());bPagar.setProgress(-1);}
                    });

                }
                if(costo<tarifa){
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
                                nombreEmpresa,domicilioEmpresa,rfcEmpresa,agente,ruta,
                                quienOcupa,contribuyente,control,propietario);

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



    public Boolean required(EditText editText) {
        String str = editText.getText().toString();
        if (str.equalsIgnoreCase("")) {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }



}

