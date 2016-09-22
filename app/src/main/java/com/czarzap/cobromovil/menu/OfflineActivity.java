package com.czarzap.cobromovil.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.Rutas;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.search.Contribuyente;
import com.czarzap.cobromovil.service.OfflineService;
import com.czarzap.cobromovil.utils.OfflineUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineActivity extends AppCompatActivity {
    int onStartCount = 0;
    Button bDownload,bUpload;
    Integer empresa,numero;
    private OfflineService service;
    List<InComercios> comercios;
    List<Contribuyente> contribuyentes;
    List<Rutas> rutas;
    OfflineUtil util = new OfflineUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
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
        initViews();
    }


    private void initViews(){
        DatabaseManager manager = new DatabaseManager(this);
        empresa = manager.getEmpresa();
        numero = manager.getAgente();
        bDownload = (Button) findViewById(R.id.bDescarga);
        bUpload = (Button) findViewById(R.id.bCarga);

        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(OfflineService.class);  // El servicio del Datos Comercio

        bDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            download();
            }
        });

        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void download(){
    Call<List<InComercios>> call = service.downloadComercios(empresa,numero);
        call.enqueue(new Callback<List<InComercios>>() {
           @Override
           public void onResponse(Call<List<InComercios>> call, Response<List<InComercios>> response) {
              comercios = response.body();
              util.initDownloadComercios(comercios,getApplicationContext());
           }

           @Override
           public void onFailure(Call<List<InComercios>> call, Throwable t) {
               Log.d("Error Comercio",t.getMessage());
           }
        });

    Call<List<Contribuyente>> callContribuyente = service.downloadContribuyentes(empresa,numero);
        callContribuyente.enqueue(new Callback<List<Contribuyente>>() {
            @Override
            public void onResponse(Call<List<Contribuyente>> call, Response<List<Contribuyente>> response) {
                contribuyentes = response.body();
                util.initDownloadContribuyentes(contribuyentes,getApplicationContext());
            }

            @Override
            public void onFailure(Call<List<Contribuyente>> call, Throwable t) {
                Log.d("Error Contribuyentes",t.getMessage());
            }
        });

    Call<List<Rutas>> callRutas = service.downloadRutas(empresa);
        callRutas.enqueue(new Callback<List<Rutas>>() {
            @Override
            public void onResponse(Call<List<Rutas>> call, Response<List<Rutas>> response) {
                rutas = response.body();
                util.initDownloadRutas(rutas,getApplicationContext());
            }

            @Override
            public void onFailure(Call<List<Rutas>> call, Throwable t) {
                Log.d("Error Rutas",t.getMessage());
            }
        });

        try {
            util.rutasData(getApplicationContext());
            util.comerciosData(getApplicationContext());
            util.contribuyentesData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
