package com.czarzap.cobromovil.menu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.service.OfflineService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineActivity extends AppCompatActivity {
    int onStartCount = 0;
    Button bDownload,bUpload;
    Integer empresa;
    private OfflineService service;
    List<InComercios> comercios;

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
                try {
                    upload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    static String fileName = "comercios.txt";
    private void download(){
    Call<List<InComercios>> call = service.download(empresa);
      call.enqueue(new Callback<List<InComercios>>() {
          @Override
          public void onResponse(Call<List<InComercios>> call, Response<List<InComercios>> response) {
              comercios = response.body();
              initDownload(comercios);
          }

          @Override
          public void onFailure(Call<List<InComercios>> call, Throwable t) {

          }
      });
    }

    private void initDownload(List<InComercios> comercios){
        String filename = "myfile.txt";

        Gson gson = new Gson();
        String s = gson.toJson(comercios);

        FileOutputStream outputStream;

        try {
            outputStream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void upload() throws IOException {

        FileInputStream fis = getApplicationContext().openFileInput("myfile.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<InComercios>>(){}.getType();
        List<InComercios> c = gson.fromJson(json, listType);
        for(InComercios comercio : c){
            Log.d("Comercio",comercio.getCom_control().toString());
        }
    }



}
