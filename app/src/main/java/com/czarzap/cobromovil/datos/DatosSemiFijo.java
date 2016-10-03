package com.czarzap.cobromovil.datos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.pagos.PagosSemiFijo;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.dd.CircularProgressButton;


public class DatosSemiFijo extends BaseActivity{

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
    private LinearLayout linearLayout;
    int onStartCount = 0;
    private ProgressBar progress;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_datos_comercio);
        linearLayout = (LinearLayout) findViewById(R.id.Linear) ;
        onStartCount = 1;
        builder = new AlertDialog.Builder(this);
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
                            Intent menuIntent = new Intent(DatosSemiFijo.this,MenuActivity.class);
                            DatosSemiFijo.this.startActivity(menuIntent);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else loadActivity();
            }
        }
        else{
            loadActivity();
        }


    }
    private void loadActivity(){
        String local;
        if(comercio.getCom_local() != null){
                    if (comercio.getCom_local().equals("S")) local = "LOCAL";
                    else local = "EXTERNO";
                    tvLocal.setText(local);
        }
        else tvLocal.setVisibility(View.GONE);
        if (comercio.getCom_nombre_propietario() != null)       etPropietario.setText(comercio.getCom_nombre_propietario());
        if (comercio.getCom_domicilio() != null)                etDomicilio.setText(comercio.getCom_domicilio());
        if (comercio.getCom_domicilio_notificaciones() != null) etDomicilioNotif.setText(comercio.getCom_domicilio_notificaciones());
        if (comercio.getCom_colonia() != null)                  etColonia.setText(comercio.getCom_colonia());
        if (comercio.getCom_frente() != null)                   etFrente.setText(String.valueOf(comercio.getCom_frente().toString()));
        if (comercio.getCom_fondo() != null)                    etFondo.setText(String.valueOf(comercio.getCom_fondo()));
        if (comercio.getCom_ocupante() != null)                 etQuien.setText(comercio.getCom_ocupante());
        if (comercio.getCom_control() != null)                  etControl.setText(String.valueOf(comercio.getCom_control()));
        if (comercio.getCom_ult_eje() != null)                  etLicencia.setText(String.valueOf(comercio.getCom_ult_eje()));
        if (comercio.getCom_ruta() != null)                     etRuta.setText(comercio.getCom_ruta());
        if(comercio.getCom_status() != null){
            if (comercio.getCom_status().equals("A"))           sStatus.setChecked(true);
            else sStatus.setChecked(false);
        }
        progress.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        bSiguiente.setVisibility(View.VISIBLE);
        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sStatus.isChecked()) {
                    Log.d("Comercio",comercio.toString());
                    Intent comercioIntent = new Intent(DatosSemiFijo.this, PagosSemiFijo.class);
                    comercioIntent.putExtra("comercio",comercio);
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
