package com.czarzap.cobromovil.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.adapter.AdapterComercio;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.service.DatosComercioService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaComercioActivity  extends Activity {

    private AdapterComercio adapter;
    private DatosComercioService service;
    private ProgressBar progress;
    Bundle args;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_lista_comercio );
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        progress=(ProgressBar)findViewById(R.id.progress_barcomercio);
        final DatabaseManager manager = new DatabaseManager ( this );  // llamar a la Base
        String url = manager.getWebService ( 1 );
        loadJSON(url);

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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvListComercio);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter (adapter);
        progress.setVisibility(View.GONE);


    }

    private void loadJSON(String url) {

        Retrofit retrofit = new Retrofit.Builder ()                          // Crear REST
                .baseUrl ( url )
                .addConverterFactory ( GsonConverterFactory.create () )
                .build ();

        service  = retrofit.create ( DatosComercioService.class );  // El servicio del Datos
        String ruta = getIntent ().getExtras ().getString ( "id" );
        args = getIntent().getExtras();
        conRutas(ruta);


    }

    private void conRutas(String ruta){
        final ArrayList<String> rutas = getIntent ().getExtras ().getStringArrayList ( "rutas" );

        Call<List<InComercios>> call = service.getListComerciosSemiFijo (ruta);

        call.enqueue ( new Callback<List<InComercios>>() {
            @Override
            public void onResponse(Call<List<InComercios>> call, Response<List<InComercios>> response) {
                List<InComercios> comercios = response.body ();
                adapter = new AdapterComercio (comercios,args);
                initViews ();
            }

            @Override
            public void onFailure(Call<List<InComercios>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        } );

    }

}
