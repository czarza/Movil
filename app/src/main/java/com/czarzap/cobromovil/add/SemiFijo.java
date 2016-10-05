package com.czarzap.cobromovil.add;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;


public class SemiFijo extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initValues();
        return inflater.inflate(R.layout.fragment_semi_fijo, container, false);

    }

    private void initValues(){

    }

}
