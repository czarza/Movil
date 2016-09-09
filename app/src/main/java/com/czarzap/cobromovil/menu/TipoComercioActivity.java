package com.czarzap.cobromovil.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.main.HeatSensitiveActivity;
import com.czarzap.cobromovil.search.BuscarContribuyente;

public class TipoComercioActivity extends Activity {

    private Button bSemifijo;
    private Button bFijo;
    private Button bCorte;
    private Integer empresa;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_tipo_comercio);
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

    private void initView(){
        DatabaseManager manager = new DatabaseManager(this);
        empresa = manager.getEmpresa();
        bFijo = (Button) findViewById(R.id.bEstablecido);
        bSemifijo     = (Button) findViewById(R.id.bSemiFijo);
        bCorte = (Button) findViewById(R.id.bCorte);

        loadActivity();
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
    private void loadActivity(){
        bFijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoComercioActivity.this,BuscarContribuyente.class);
                TipoComercioActivity.this.startActivity(intent);
            }
        });
        bSemifijo.setOnClickListener(new View.OnClickListener() {  // Click Listener Qr
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("empresa",empresa);
                Intent comercioIntent = new Intent(TipoComercioActivity.this,ListaRutasActivity.class);
                comercioIntent.putExtras ( args );
                TipoComercioActivity.this.startActivity(comercioIntent);
            }
        });
        bCorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}