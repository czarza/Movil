package com.czarzap.cobromovil.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.main.HeatSensitiveActivity;


public class MenuActivity extends Activity{

    private static final int CAMERA_REQUEST = 1888;
    private  Button bLeerQr;
    private  Button bComercio;
    private  Button bConfigurar;
    private Integer empresa;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_menu);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        initView();
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

    private void initView(){
        DatabaseManager manager = new DatabaseManager(this);
        empresa = manager.getEmpresa();

        bLeerQr     = (Button) findViewById(R.id.bLeerQr);
        bComercio   = (Button) findViewById(R.id.bComercio);
        bConfigurar   = (Button) findViewById(R.id.bConfigurar);

        loadActivity();
    }

    private void loadActivity(){

        bLeerQr.setOnClickListener(new View.OnClickListener() {  // Click Listener Qr
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(MenuActivity.this,LeerQrActivity.class);
                startActivityForResult(comercioIntent, CAMERA_REQUEST);
            }
        });



        bComercio.setOnClickListener(new View.OnClickListener() { // Click Comercio
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("empresa",empresa);
                Intent rutasIntent = new Intent(MenuActivity.this,TipoComercioActivity.class);
                rutasIntent.putExtras ( args );
                MenuActivity.this.startActivity(rutasIntent);
            }
        });


        bConfigurar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("empresa",empresa);
                Intent configurarIntent = new Intent(MenuActivity.this,HeatSensitiveActivity.class);
                configurarIntent.putExtras ( args);
                MenuActivity.this.startActivity(configurarIntent);
            }
        } );
    }

}
