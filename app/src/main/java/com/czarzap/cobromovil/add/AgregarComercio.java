package com.czarzap.cobromovil.add;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
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
import java.util.ArrayList;
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
    Spinner spinner;
    List<String> items;
    RadioButton semi,ambulante;
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
        semi = (RadioButton) findViewById(R.id.rbSemi);
        ambulante = (RadioButton) findViewById(R.id.rbAmbulante);
        OfflineUtil util = new OfflineUtil();
        try {
            List<InMetaCampos> rutas = util.rutasData(getApplicationContext());
            items = new ArrayList<>();
            for(InMetaCampos campo : rutas){
                items.add(campo.getMc_nombre());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        manager = new DatabaseManager(this);

        Fragment fragment;
        Log.d("ITEMS",items.toString());
        if(items.isEmpty()){
            ambulante.setChecked(true);
            semi.setChecked(false);
            fragment = new Ambulante();
        }
        else{
            semi.setChecked(true);
            ambulante.setChecked(false);
            fragment = new SemiFijo();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentComercio, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        agregarComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) findViewById(R.id.rbSemi);
                if(rb.isChecked()) agregarSemifijo();
                else agregarAmbulante();
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
        if (view == findViewById(R.id.rbSemi)){
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

    private void agregarAmbulante(){
        Integer idContribuyente = (Integer) getIntent().getExtras().get("id");
        EditText propietario = (EditText) findViewById(R.id.etAPropietario);
        EditText domicilio = (EditText) findViewById(R.id.etADomicilio);

        Integer empresa = manager.getEmpresa();
        InComercios comercio = new InComercios();
        comercio.setCom_empresa(empresa);
        comercio.setCom_tipo("A");
        comercio.setCom_contribuyente(idContribuyente);
        comercio.setCom_nombre_propietario(propietario.getText().toString());
        comercio.setCom_domicilio_notificaciones(domicilio.getText().toString());
        sendComercio(comercio);
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
        if(spinner.getSelectedItem().toString() != null)  comercio.setCom_ruta(spinner.getSelectedItem().toString());
        sendComercio(comercio);
    }

    private void sendComercio(InComercios comercio) {
        String url = manager.getWebService(1);
        comercio.setAgente(manager.getAgente());
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
                    Toast.makeText(getApplicationContext(), "No tiene permiso para agregar Comercio", Toast.LENGTH_LONG).show();
                    agregarComercio.setProgress(-1);
                }
            }

            @Override
            public void onFailure(Call<InComercios> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
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
