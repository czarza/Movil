package com.czarzap.cobromovil.add;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.search.Contribuyente;
import com.czarzap.cobromovil.service.AgregarService;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.dd.CircularProgressButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AgregarContribuyente extends AppCompatActivity {
    EditText propietario,domicilio;
    CircularProgressButton agregar,regresar,nuevo,comercio;
    DatabaseManager manager ;
    LinearLayout linear;
    Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Nuevo Contribuyente");
        setContentView(R.layout.activity_agregar_contribuyente);

        propietario = (EditText) findViewById(R.id.etNombre);
        domicilio = (EditText) findViewById(R.id.etDomicilio);
        agregar = (CircularProgressButton) findViewById(R.id.bAgregar);
        regresar = (CircularProgressButton) findViewById(R.id.bCRegresar);
        nuevo = (CircularProgressButton) findViewById(R.id.bCAlta);
        comercio = (CircularProgressButton) findViewById(R.id.bCComercio);
        linear = (LinearLayout) findViewById(R.id.LAgregarContribuyente);
        manager = new DatabaseManager(this);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer empresa = manager.getEmpresa();
                String nombre = propietario.getText().toString();
                String dom = domicilio.getText().toString();
                sendContribuyente(empresa,nombre,dom);
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(AgregarContribuyente.this,MenuActivity.class);
                AgregarContribuyente.this.startActivity(comercioIntent);
            }
        });
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(AgregarContribuyente.this,Agregar.class);
                AgregarContribuyente.this.startActivity(comercioIntent);
            }
        });
        comercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgregarContribuyente.this,AgregarComercio.class);
                intent.putExtra("id",ID);
                startActivity(intent);
            }
        });

    }

    private void sendContribuyente(Integer empresa, String nombre, String dom) {
        String url = manager.getWebService(1);
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AgregarService service = retrofit.create(AgregarService.class);
        Call<Contribuyente> call = service.agregarContribuyente(empresa,nombre,dom);
        call.enqueue(new Callback<Contribuyente>() {
            @Override
            public void onResponse(Call<Contribuyente> call, Response<Contribuyente> response) {

                if(response.body() != null){
                    try {
                        Contribuyente contribuyente = response.body();
                        ID = contribuyente.getId();
                        saveContribuyente(contribuyente);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "No tiene permiso para agregar Contribuyentes", Toast.LENGTH_LONG).show();
                    agregar.setProgress(-1);
                }
            }

            @Override
            public void onFailure(Call<Contribuyente> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, no tiene conexion a Internet", Toast.LENGTH_LONG).show();
                agregar.setProgress(-1);
            }
        });

    }

    private void saveContribuyente(Contribuyente contribuyente) throws IOException {
        OfflineUtil util = new OfflineUtil();
        List<Contribuyente> contribuyentes = util.contribuyentesData(getApplicationContext());
        contribuyentes.add(contribuyente);
        util.initDownloadContribuyentes(contribuyentes,getApplicationContext());
        agregar.setProgress(100);
        agregar.setEnabled(false);
        linear.setVisibility(View.VISIBLE);

    }


}
