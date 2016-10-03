package com.czarzap.cobromovil.add;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.R;

public class Agregar extends AppCompatActivity {
    private Button bContribuyente;
    private Button bComercio;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        getSupportActionBar().setTitle("Agregar");

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
        bContribuyente = (Button) findViewById(R.id.bContribuyente);
        bComercio = (Button) findViewById(R.id.bAComercio);

        bContribuyente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configurarIntent = new Intent(Agregar.this,AgregarContribuyente.class);
                startActivity(configurarIntent);
            }
        });

        bComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configurarIntent = new Intent(Agregar.this,AgregarComercio.class);
                startActivity(configurarIntent);
            }
        });
    }

}
