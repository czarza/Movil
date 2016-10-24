package com.czarzap.cobromovil.offline;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.search.Contribuyente;
import com.czarzap.cobromovil.service.OfflineService;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineActivity extends BaseActivity {
    int onStartCount = 0;
    private Bitmap bmp;
    private OfflineService service;
    CircularProgressButton bUpload;
    CheckBox com,con,ru,pa,log;
    Integer empresa,numero;
    List<InComercios> comercios;
    List<Contribuyente> contribuyentes;
    List<InMetaCampos> rutas;
    OfflineUtil util = new OfflineUtil();
    DatabaseManager manager;


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
        initViews();
    }


    private void initViews(){
        manager = new DatabaseManager(this);
        empresa = manager.getEmpresa();
        numero = manager.getAgente();
        bUpload = (CircularProgressButton) findViewById(R.id.bCarga);
        pa = (CheckBox) findViewById(R.id.checkBoxPagos) ;
        com = (CheckBox) findViewById(R.id.checkBoxComercios) ;
        con = (CheckBox) findViewById(R.id.checkBoxContribuyentes) ;
        ru= (CheckBox) findViewById(R.id.checkBoxRutas) ;
        log = (CheckBox) findViewById(R.id.checkBoxLogo);
        String url = manager.getWebService(1);                              // obtener el webService de Comercio
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(OfflineService.class);  // El servicio del Datos Comercio

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

    private void getLogo(){
        Integer empresa = manager.getEmpresa();
        Call<ResponseBody> call = service.getLogo(empresa);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ALPHA_8;
                bmp = BitmapFactory.decodeStream(response.body().byteStream(), null, options);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                log.setChecked(true);
                try {
                    saveToInternalStorage(bmp);
                    getRutas();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,"logo.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    private void getContribuyentes(){
        Log.d("numero",numero.toString());
        Call<List<Contribuyente>> callContribuyente = service.downloadContribuyentes(empresa,numero);
        callContribuyente.enqueue(new Callback<List<Contribuyente>>() {
            @Override
            public void onResponse(Call<List<Contribuyente>> call, Response<List<Contribuyente>> response) {
                contribuyentes = response.body();
                Log.d("Contribuyentes",contribuyentes.toString());
                util.initDownloadContribuyentes(contribuyentes,getApplicationContext());
                con.setChecked(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            getComercios();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 800);
            }

            @Override
            public void onFailure(Call<List<Contribuyente>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Revise su Conexion, no se ha descargado los Contribuyentes", Toast.LENGTH_LONG).show();
                bUpload.setProgress(-1);
            }
        });

    }

    private void getRutas(){
        Call<List<InMetaCampos>> callRutas = service.downloadRutas(empresa,numero);
        callRutas.enqueue(new Callback<List<InMetaCampos>>() {
            @Override
            public void onResponse(Call<List<InMetaCampos>> call, Response<List<InMetaCampos>> response) {
                rutas = response.body();
                Log.d("rutas",rutas.toString());
                util.initDownloadRutas(rutas,getApplicationContext());
                ru.setChecked(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getContribuyentes();
                    }
                }, 800);


            }

            @Override
            public void onFailure(Call<List<InMetaCampos>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Revise su Conexion, no se ha descargado las Rutas", Toast.LENGTH_LONG).show();
                bUpload.setProgress(-1);
            }
        });

    }

    private void getComercios() throws IOException {
        if(util.fileExistsComercio(getApplicationContext())){
            List<InComercios> enviarComercios = util.comerciosData(getApplicationContext());

            if(!enviarComercios.isEmpty()){
                enviarComercios.get(0).setAgente(manager.getAgente());
            }
            
            Gson gson = new Gson();

            String json = gson.toJson(enviarComercios);
            Call<List<InComercios>> call = service.downloadComercios(json);
            call.enqueue(new Callback<List<InComercios>>() {
                @Override
                public void onResponse(Call<List<InComercios>> call, Response<List<InComercios>> response) {
                    comercios = response.body();
                    Log.d("Comercio",comercios.toString());
                    util.initDownloadComercios(comercios,getApplicationContext());
                    com.setChecked(true);
                    bUpload.setProgress(100);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent menuIntent = new Intent(OfflineActivity.this, MenuActivity.class);
                            OfflineActivity.this.startActivity(menuIntent);
                            bUpload.setProgress(0);
                        }
                    }, 800);

                }

                @Override
                public void onFailure(Call<List<InComercios>> call, Throwable t) {
                    Log.d("Descarga Comercios",t.getMessage());
                    Toast.makeText(getApplicationContext(), "Revise su Conexion, no se ha descargado los Comercios", Toast.LENGTH_LONG).show();
                    bUpload.setProgress(-1);
                }
            });
        }


        else{
            Call<List<InComercios>> call = service.donwloadComerciosnoUpdate(empresa,numero);
            call.enqueue(new Callback<List<InComercios>>() {
                @Override
                public void onResponse(Call<List<InComercios>> call, Response<List<InComercios>> response) {
                    comercios = response.body();
                    Log.d("Comercio",comercios.toString());
                    util.initDownloadComercios(comercios,getApplicationContext());
                    com.setChecked(true);
                    bUpload.setProgress(100);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent menuIntent = new Intent(OfflineActivity.this, MenuActivity.class);
                            OfflineActivity.this.startActivity(menuIntent);
                            bUpload.setProgress(0);
                        }
                    }, 800);
                }

                @Override
                public void onFailure(Call<List<InComercios>> call, Throwable t) {
                    Log.d("Descarga Comercios",t.getMessage());
                    Toast.makeText(getApplicationContext(), "Revise su Conexion, no se ha descargado los Comercios", Toast.LENGTH_LONG).show();
                    bUpload.setProgress(-1);
                }
            });

        }

    }

    private void setFolio(){
        Integer folio = manager.getFolio();
        Call<String> call = service.setFolio(empresa,numero,folio);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {}

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Revise su Conexion, no se ha descargado los Comercios", Toast.LENGTH_LONG).show();}
        });
    }

    private void download() {
        bUpload.setProgress(50);
        getLogo();
    }

    private void upload() throws IOException {
        bUpload.setIndeterminateProgressMode(true);
        if(util.fileExistPagos(getApplicationContext())){
            List<InComercio_cobro_movil> pagos = util.pagosData(getApplicationContext());
            Gson gson = new Gson();
            String json = gson.toJson(pagos);
            Call<String> call = service.upload(json);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String respuesta = response.body();
                    if(respuesta == null){
                        Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
                    }
                    else{
                        pa.setChecked(true);
                        setFolio();
                        try {
                            util.PagosDelete(getApplicationContext());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        download();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    bUpload.setProgress(-1);
                    Log.d("UPLOAD",t.getMessage());
                    Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "No hay pagos que enviar", Toast.LENGTH_LONG).show();
            download();
        }
    }


}
