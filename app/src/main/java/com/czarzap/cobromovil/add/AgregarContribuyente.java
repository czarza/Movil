package com.czarzap.cobromovil.add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.czarzap.cobromovil.R;
import com.dd.CircularProgressButton;


public class AgregarContribuyente extends AppCompatActivity {
    EditText propietario,domicilio;
    CircularProgressButton agregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Nuevo Contribuyente");
        setContentView(R.layout.activity_agregar_contribuyente);

        propietario = (EditText) findViewById(R.id.etNombre);
        domicilio = (EditText) findViewById(R.id.etDomicilio);
        agregar = (CircularProgressButton) findViewById(R.id.bAgregar);

        a

    }
}
