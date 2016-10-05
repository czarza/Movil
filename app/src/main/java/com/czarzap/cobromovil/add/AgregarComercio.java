package com.czarzap.cobromovil.add;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.datos.DatosAmbulante;
import com.czarzap.cobromovil.datos.DatosEstablecido;
import com.czarzap.cobromovil.datos.DatosMotos;
import com.czarzap.cobromovil.datos.DatosSemiFijo;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.service.AgregarService;
import com.czarzap.cobromovil.utils.OfflineUtil;
import com.dd.CircularProgressButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarComercio extends AppCompatActivity {
    CircularProgressButton agregarComercio,alta,regresar,cobrar;
    LinearLayout menu;
    DatabaseManager manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().setTitle("Nuevo Comercio");
        setContentView(R.layout.activity_agregar_comercio);

        agregarComercio = (CircularProgressButton) findViewById(R.id.bAddComercio);
        alta = (CircularProgressButton) findViewById(R.id.bANuevo);
        regresar = (CircularProgressButton) findViewById(R.id.bARegresar);
        cobrar = (CircularProgressButton) findViewById(R.id.bACobrar);
        menu = (LinearLayout) findViewById(R.id.LAgregar);
        manager = new DatabaseManager(getApplicationContext());
        Fragment fragment;
        fragment = new Establecido();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentComercio, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


        agregarComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSemifijo();
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(AgregarComercio.this,MenuActivity.class);
                AgregarComercio.this.startActivity(comercioIntent);
            }
        });
        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comercioIntent = new Intent(AgregarComercio.this,Agregar.class);
                AgregarComercio.this.startActivity(comercioIntent);
            }
        });

    }

    public void ChangeFragment(View view){
        Fragment fragment;
        if(view == findViewById(R.id.rbEstablecido)){
            fragment = new Establecido();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentComercio, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (view == findViewById(R.id.rbSemi)){
            fragment = new SemiFijo();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentComercio, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            fragment = new Ambulante();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentComercio, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }


    private void agregarSemifijo(){
        Integer idContribuyente = (Integer) getIntent().getExtras().get("id");
        EditText propietario = (EditText) findViewById(R.id.etSNombre);
        EditText domicilio = (EditText) findViewById(R.id.etSDomicilio);
        EditText ocupante = (EditText) findViewById(R.id.etSOcupante);
        EditText frente = (EditText) findViewById(R.id.etSFrente);
        EditText fondo = (EditText) findViewById(R.id.etSFondo);
        Integer empresa = manager.getEmpresa();
        InComercios comercio = new InComercios();
        comercio.setCom_empresa(empresa);
        comercio.setCom_tipo("S");
        comercio.setCom_contribuyente(idContribuyente);
        comercio.setCom_nombre_propietario(propietario.getText().toString());
        comercio.setCom_domicilio_notificaciones(domicilio.getText().toString());
        comercio.setCom_ocupante(ocupante.getText().toString());
        comercio.setCom_frente(new BigDecimal (frente.getText().toString()));
        comercio.setCom_fondo(new BigDecimal(fondo.getText().toString()));

        sendComercio(comercio);
    }

    private void sendComercio(InComercios comercio) {
        String url = manager.getWebService(1);
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AgregarService service = retrofit.create(AgregarService.class);

        Call<InComercios> call = service.agregarComercio(comercio);
        call.enqueue(new Callback<InComercios>() {
            @Override
            public void onResponse(Call<InComercios> call, Response<InComercios> response) {
                InComercios comercio = response.body();
                if(comercio.getCom_control() != null){
                    try {
                        saveComercio(comercio);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    // No se agrego el numero de Control del comercio
                    agregarComercio.setProgress(-1);
                }
            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                // Fallo de Conexion
            }
        });


    }

    private void saveComercio(final InComercios comercio) throws IOException {
        OfflineUtil util = new OfflineUtil();
        List<InComercios> comercios = util.comerciosData(getApplicationContext());
        comercios.add(comercio);
        util.initDownloadComercios(comercios,getApplicationContext());
        agregarComercio.setProgress(100);
        menu.setVisibility(View.VISIBLE);

        cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swicth(comercio);
            }
        });
    }

    private void swicth(InComercios comercio) {
        Intent intent;
        switch (comercio.getCom_tipo()){
            case "S":
                intent = new Intent(getApplicationContext(), DatosSemiFijo.class);
                intent.putExtra("comercio",comercio);
                getApplicationContext().startActivity(intent);
                break;
            case "F":
                intent = new Intent(getApplicationContext (), DatosEstablecido.class);
                intent.putExtra("comercio",comercio);
                getApplicationContext().startActivity(intent);
                break;
            case "A":
                intent = new Intent(getApplicationContext(), DatosAmbulante.class);
                intent.putExtra("comercio",comercio);
                getApplicationContext().startActivity(intent);
                break;
            case "M":
                intent = new Intent(getApplicationContext(), DatosMotos.class);
                intent.putExtra("comercio",comercio);
                getApplicationContext().startActivity(intent);
                break;
        }
    }
}
