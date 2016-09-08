package com.czarzap.cobromovil.menu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.adapter.AdapterRutas;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.service.DatosComercioService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaRutasActivity extends Activity  {

    private RecyclerView recyclerView;
    private AdapterRutas adapterRutas;
    private ProgressBar progress;
    private List<InMetaCampos> campos;
    Bundle args;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_lista_rutas );
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        final DatabaseManager manager = new DatabaseManager ( this );  // llamar a la Base
        Integer empresa = getIntent ().getExtras ().getInt ( "empresa" );
        String url = manager.getWebService ( 1 );                              // obtener el webService de Comercio
        loadJSON(empresa,url);
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
    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.rvListRutas);
        progress=(ProgressBar)findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapterRutas = new AdapterRutas (campos,args);
                recyclerView.setAdapter (adapterRutas );
                progress.setVisibility(View.GONE);

            }
        }, 800);


    }

    private void loadJSON(Integer empresa,String url) {

        Retrofit retrofit = new Retrofit.Builder ()                          // Crear REST
                .baseUrl ( url )
                .addConverterFactory ( GsonConverterFactory.create () )
                .build ();

        final DatosComercioService service = retrofit.create ( DatosComercioService.class );  // El servicio del Datos

        Call<List<InMetaCampos>> call = service.getListRutas (empresa);

        call.enqueue ( new Callback<List<InMetaCampos>> () {
            @Override
            public void onResponse(Call<List<InMetaCampos>> call, Response<List<InMetaCampos>> response) {
                campos = response.body ();
                args  = new Bundle();
                for(InMetaCampos campo:campos){
                    args.putString(campo.getMc_campo(),campo.getMc_nombre());
                }
                initViews();

            }

            @Override
            public void onFailure(Call<List<InMetaCampos>> call, Throwable t) {
                Log.d ( "RutasActivity", t.getMessage () );
            }

        } );



    }


}
