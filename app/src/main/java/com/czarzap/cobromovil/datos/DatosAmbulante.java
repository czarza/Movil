package com.czarzap.cobromovil.datos;

import android.app.AlertDialog;
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
import com.czarzap.cobromovil.pagos.PagosAmbulante;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.dd.CircularProgressButton;



public class DatosAmbulante extends BaseActivity {

    private EditText etPropietario;
    private EditText etDomicilio;
    private EditText etColonia;
    private EditText etQuien;
    private EditText etControl;
    private EditText etLicencia;
    private CircularProgressButton bSiguiente;
    private Switch sStatus;
    private InComercios comercio;
    private Integer control,empresa;
    private String tipo;
    private LinearLayout linearLayout;
    private AlertDialog.Builder builder;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_datos_ambulante);
        linearLayout = (LinearLayout) findViewById(R.id.LinearAmbulante) ;

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

        builder = new AlertDialog.Builder(this);
        DatabaseManager manager = new DatabaseManager(this);
        etPropietario = (EditText) findViewById(R.id.aPropietario);  // Parse xml items to Class
        etDomicilio = (EditText) findViewById(R.id.aDomicilio);
        etColonia = (EditText) findViewById(R.id.aColonia);
        etQuien = (EditText) findViewById(R.id.aQuien);
        etControl = (EditText) findViewById(R.id.aControl);
        etLicencia = (EditText) findViewById(R.id.aLicencia);
        bSiguiente   = (CircularProgressButton) findViewById(R.id.aSiguiente);
        sStatus = (Switch) findViewById(R.id.aStatus);
        comercio = new InComercios();
        comercio = (InComercios) getIntent().getExtras().get("comercio");
        if(comercio == null){
            control =Integer.valueOf( getIntent().getExtras().getString("control"));                 // Leer los datos pasados por el QR
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
        builder.setMessage("No existe el Comercio Ambulante").setTitle("ERROR").setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent comercioIntent = new Intent(DatosAmbulante.this, MenuActivity.class);
                            DatosAmbulante.this.startActivity(comercioIntent);}
                    });

        AlertDialog alert = builder.create();
        alert.show();

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
        bSiguiente.setVisibility(View.VISIBLE);
        bSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(required(etPropietario)&&required(etDomicilio)&&required(etColonia)&&required(etQuien)) {
                        if(sStatus.isChecked()){
                            sendDatatoCobro();
                        }
                        else{Toast.makeText(getApplicationContext(), "El comercio debe de estar Activo", Toast.LENGTH_LONG).show();}
                    }
                    else{Toast.makeText(getApplicationContext(), "Llene todos los datos", Toast.LENGTH_LONG).show();}
                }
        });


    }


    private void sendDatatoCobro(){
                Intent comercioIntent = new Intent(DatosAmbulante.this, PagosAmbulante.class);
                comercioIntent.putExtra("comercio",comercio);
                DatosAmbulante.this.startActivity(comercioIntent);
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