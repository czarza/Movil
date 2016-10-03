package com.czarzap.cobromovil.offline;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.main.HeatSensitiveActivity;
import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.receiver.UsbDeviceReceiver;
import com.czarzap.cobromovil.service.OfflineService;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.dd.CircularProgressButton;

import java.io.IOException;

import driver.Contants;
import driver.HsBluetoothPrintDriver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends BaseActivity {
    private static final int REQUEST_ENABLE_BT = 2;
    CheckBox internet;
    CheckBox sistema;
    CheckBox rutas;
    CheckBox comercios;
    CheckBox impresora;
    CircularProgressButton bTest;
    int onStartCount = 0;
    OfflineService service;
    DatabaseManager manager;
    Integer empresa,numero;
    HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        onStartCount = 1;
        hsBluetoothPrintDriver.Begin();
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
        bTest = (CircularProgressButton) findViewById(R.id.bTest);
        internet = (CheckBox) findViewById(R.id.checkBoxInternet) ;
        sistema = (CheckBox) findViewById(R.id.checkBoxSifi) ;
        rutas = (CheckBox) findViewById(R.id.checkBoxRuta) ;
        comercios= (CheckBox) findViewById(R.id.checkBoxComercio) ;
        impresora = (CheckBox) findViewById(R.id.checkBoxImpresora);
        manager = new DatabaseManager(this);
        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        empresa = manager.getEmpresa();
        numero = manager.getAgente();
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(OfflineService.class);  // El servicio del Datos Comercio

        bTest.setIndeterminateProgressMode(true);
        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internet();
            }
        });
    }

    private void internet(){
        Call<String> call = service.test(empresa);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                internet.setChecked(true);
                if(response.body() != null) sistema.setChecked(true);
                rutas();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo de Prueba", Toast.LENGTH_LONG).show();
                bTest.setProgress(-1);
            }
        });
    }
    private void rutas(){
        Call<String> call = service.testRutas(empresa,numero);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ruta = "Rutas: "+response.body();
                rutas.setText(ruta);
                rutas.setChecked(true);
                comercio();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo de Prueba", Toast.LENGTH_LONG).show();
                bTest.setProgress(-1);
            }
        });
    }
    private void comercio(){
        Call<String> call = service.testComercios(empresa,numero);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String comercio = "Comercios: "+ response.body();
                comercios.setText(comercio);
                comercios.setChecked(true);
                impresora();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo de Prueba", Toast.LENGTH_LONG).show();
                bTest.setProgress(-1);
            }
        });
    }

    private void impresora(){
        if (RTApplication.getConnState() == Contants.UNCONNECTED) {
            Toast.makeText(getApplicationContext(), "Fallo de Prueba", Toast.LENGTH_LONG).show();
            bTest.setProgress(-1);
        }
        else{
            impresora.setChecked(true);
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


            hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
            hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
            hsBluetoothPrintDriver.SetBold((byte) 0x01);
            hsBluetoothPrintDriver.BT_Write("Prueba Exitosa");
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();

            bTest.setProgress(100);
        }
    }

}
