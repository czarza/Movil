package com.czarzap.cobromovil.comercio;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.dd.CircularProgressButton;
import com.squareup.picasso.Picasso;

public class VerImagenActivity extends AppCompatActivity {
    String tipo,empresa,control,url;
    CircularProgressButton bPrincipal , bConductor;
    ImageView imageView;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagen);
        imageView = (ImageView) findViewById(R.id.ivImagen);
        CircularProgressButton bRegresar = (CircularProgressButton)findViewById(R.id.bRegresar);
        bPrincipal = (CircularProgressButton) findViewById(R.id.bPrincipal);
        bConductor = (CircularProgressButton) findViewById(R.id.bConductor);

        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }

        DatabaseManager manager = new DatabaseManager(this);
        url = manager.getWebService(2);
        control = getIntent().getExtras().getString("control");                 // Leer los datos pasados por el QR
        tipo = getIntent().getExtras().getString("tipo");
        empresa = getIntent().getExtras().getString("empresa");// obtener el webService de Comercio
        String campo = "images/comercio/"+empresa+"/"+tipo+control+"_1.png";
        Log.d("url",url+campo);
        Picasso.with(this)
                .load(url+campo)
                .resize(250, 200)
                .into(imageView);

        if (bRegresar != null) bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        bPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campo = "images/comercio/"+empresa+"/"+tipo+control+"_1.png";
                Log.d("url",url+campo);
                Picasso.with(getApplicationContext())
                        .load(url+campo)
                        .resize(250, 200)
                        .into(imageView);
            }
        });

        bConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campo = "images/comercio/"+empresa+"/"+tipo+control+"_2.png";
                Log.d("url",url+campo);
                Picasso.with(getApplicationContext())
                        .load(url+campo)
//                        .resize(300, 350)
                        .into(imageView);
            }
        });


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
}
