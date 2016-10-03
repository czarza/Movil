package com.czarzap.cobromovil.qr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.czarzap.cobromovil.datos.DatosMotos;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.czarzap.cobromovil.datos.DatosAmbulante;
import com.czarzap.cobromovil.datos.DatosEstablecido;
import com.czarzap.cobromovil.datos.DatosSemiFijo;


public class LeerQrActivity extends Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private AlertDialog.Builder builder;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permition(this);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        builder = new AlertDialog.Builder(this);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
    }

    private void permition(LeerQrActivity leerQrActivity){
        int MY_PERMISSIONS_REQUEST_Camera=101;
        if (ContextCompat.checkSelfPermission(leerQrActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(leerQrActivity,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(leerQrActivity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_Camera);

            }
        }
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
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {  // The result of the scanning
        onPause();
        Bundle args = getQueryMap(rawResult.getText());

        String tipoQr = args.getString ( "qr" );
        String tipoComercio = args.getString("tipo");
        if (tipoQr == null) tipoQr = "";

        if(tipoQr.isEmpty ()){                   // El QR no corresponde al Sistema
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("El codigo QR no pertenece al Sistema").setTitle("ERROR").setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which) {
                    Intent menuIntent = new Intent(LeerQrActivity.this,MenuActivity.class);
                    LeerQrActivity.this.startActivity(menuIntent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        else if(tipoQr.equals("1"))              // Es un Comercio
        {
            Intent intent;
            switch (tipoComercio){
                case "S":
                        intent = new Intent(LeerQrActivity.this, DatosSemiFijo.class);
                        intent.putExtras (args);
                        LeerQrActivity.this.startActivity(intent);
                        break;
                case "F":
                        intent = new Intent(LeerQrActivity.this, DatosEstablecido.class);
                        intent.putExtras (args);
                        LeerQrActivity.this.startActivity(intent);
                        break;
                case "A":
                        intent = new Intent(LeerQrActivity.this, DatosAmbulante.class);
                        intent.putExtras (args);
                        LeerQrActivity.this.startActivity(intent);
                    break;
                case "M":
                    intent = new Intent(LeerQrActivity.this, DatosMotos.class);
                    intent.putExtras (args);
                    LeerQrActivity.this.startActivity(intent);
                    break;
            }
        }

        else if(tipoQr.equals("2"))            // Es un pago
        {
                Log.d("hola","hola");
        }

    }

    public static Bundle getQueryMap(String query) // Parse the String scan to Map
    {
        query = query.replaceAll("\\s+","");
        Bundle args = new Bundle();
        if(query.contains("&")&&query.contains("=")){
            String[] params = query.split("&");
            for (String param : params)
            {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                args.putString(name, value);
            }

        }
        return args;
    }
}
