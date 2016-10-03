package com.czarzap.cobromovil.add;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.czarzap.cobromovil.R;

public class AgregarComercio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().setTitle("Nuevo Comercio");
        setContentView(R.layout.activity_agregar_comercio);
        Fragment fragment;
        fragment = new Establecido();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentComercio, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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


}
