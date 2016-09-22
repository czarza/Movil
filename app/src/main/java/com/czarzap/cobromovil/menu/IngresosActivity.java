package com.czarzap.cobromovil.menu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;

public class IngresosActivity extends Activity {
    private Button bPredial;
    private  Button bComercio;
    private  Button bAgua;
    int onStartCount = 0;
    Integer empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);
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
        bPredial     = (Button) findViewById(R.id.bPredial);
        bComercio   = (Button) findViewById(R.id.bComercio);
        bAgua   = (Button) findViewById(R.id.bAgua);

        loadActivity();
    }

    private void loadActivity(){
        bPredial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("empresa",empresa);
                Intent rutasIntent = new Intent(IngresosActivity.this,TipoComercioActivity.class);
                rutasIntent.putExtras ( args );
                IngresosActivity.this.startActivity(rutasIntent);
            }
        });

        bAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
