package com.czarzap.cobromovil.add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.czarzap.cobromovil.R;


public class AgregarContribuyente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Nuevo Contribuyente");
        setContentView(R.layout.activity_agregar_contribuyente);
    }
}
