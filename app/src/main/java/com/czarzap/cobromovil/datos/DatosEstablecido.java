package com.czarzap.cobromovil.datos;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.czarzap.cobromovil.utils.Util;
import com.dd.CircularProgressButton;

import driver.HsBluetoothPrintDriver;

public class DatosEstablecido  extends BaseActivity {
    private EditText etPropietario;
    private EditText etDomicilio;
    private EditText etColonia;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private CircularProgressButton bEstado;
    private Switch sStatus;
    private InComercios comercio;
    private Integer control;
    private Integer empresa;
    private String tipo;
    private LinearLayout linearLayout;
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
        DatabaseManager manager = new DatabaseManager(this);
        etPropietario = (EditText) findViewById(R.id.etPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.etDomicilio);
        etColonia = (EditText) findViewById(R.id.etColonia);
        etQuien = (EditText) findViewById(R.id.etQuien);
        etControl = (EditText) findViewById(R.id.etControl);
        etLicencia = (EditText) findViewById(R.id.etLicencia);
        bEstado   = (CircularProgressButton) findViewById(R.id.bEstado);
        sStatus = (Switch) findViewById(R.id.sStatus);
        comercio = new InComercios();
        comercio = (InComercios) getIntent().getExtras().get("comercio");
        if(comercio == null){
            control = Integer.valueOf(getIntent().getExtras().getString("control"));                 // Leer los datos pasados por el QR
            tipo = getIntent().getExtras().getString("tipo");
            empresa = Integer.valueOf(getIntent().getExtras().getString("empresa"));

            OfflineUtil util = new OfflineUtil();
            if(util.fileExistsComercio(getApplicationContext())){
                comercio = util.getComercioOffline(getApplicationContext(),control,tipo);
                if(comercio == null) loadActivity();
                else initComercio();
            }
        }
        else{
            initComercio();
        }
    }
    private void loadActivity(){

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

    private void initComercio(){
        if (comercio.getCom_nombre_propietario() != null)            etPropietario.setText(comercio.getCom_nombre_propietario());
        if (comercio.getCom_domicilio_notificaciones() != null)      etDomicilio.setText(comercio.getCom_domicilio_notificaciones());
        if (comercio.getCom_colonia() != null)                       etColonia.setText(comercio.getCom_colonia());
        if (comercio.getCom_ocupante() != null)                      etQuien.setText(comercio.getCom_ocupante());
        if (comercio.getCom_control() != null)                       etControl.setText(String.valueOf(comercio.getCom_control()));
        if (comercio.getCom_ult_eje() != null)                       etLicencia.setText(String.valueOf(comercio.getCom_ult_eje()));
        if (comercio.getCom_status().equals("A"))                    sStatus.setChecked(true);
        else sStatus.setChecked(false);

        linearLayout.setVisibility(View.VISIBLE);
            bEstado.setVisibility(View.VISIBLE);
            bEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    util.printEstadoCuenta(comercio,getApplicationContext(),hsBluetoothPrintDriver);
                }
            });
    }
}