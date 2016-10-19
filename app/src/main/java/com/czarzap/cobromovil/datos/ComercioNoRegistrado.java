package com.czarzap.cobromovil.datos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.pagos.PagosAmbulante;
import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.List;

public class ComercioNoRegistrado extends BaseActivity {
    int onStartCount = 0;
    Spinner spinner;
    DatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comercio_no_registrado);
        manager = new DatabaseManager(this);
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

    private void initViews() {

        spinner = (Spinner) findViewById(R.id.spinnerTipo);
        List<String> items = new ArrayList<String>();
        items.add("AMBULANTE");
        items.add("SEMI-FIJO");
        items.add("BICI-TAXI");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        CircularProgressButton cobrar = (CircularProgressButton) findViewById(R.id.bComercioNoRegistrado);
        cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer empresa = manager.getEmpresa();
                String tipo = tipo(spinner.getSelectedItem().toString());
                Integer control = 0;
                InComercios comercio = new InComercios(empresa,tipo,control);
                comercio.setCom_nombre_propietario("PUBLICO EN GENERAL");
                comercio.setCom_ocupante("PUBLICO EN GENERAL");
                comercio.setNombreRuta("SIN RUTA");

                Intent intent = new Intent(ComercioNoRegistrado.this, PagosAmbulante.class);
                intent.putExtra("comercio",comercio);
                startActivity(intent);


            }
        });

    }


    private String tipo(String tipo){
        String t = null;
        switch (tipo){
            case "AMBULANTE" :
                    t = "A";
            break;
            case "SEMI-FIJO" :
                t = "S";
            break;
            case "BICI-TAXI" :
                t = "M";
            break;

        }
        return t;
    }


}
