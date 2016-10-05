package com.czarzap.cobromovil.add;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.utils.OfflineUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SemiFijo extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_semi_fijo, container, false);


        Spinner spinner = (Spinner) v.findViewById(R.id.spinnerRutas);
        OfflineUtil util = new OfflineUtil();
        List<InMetaCampos> rutas = null;
        try {
            rutas = util.rutasData(getActivity().getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> items = new ArrayList<String>();
        for(InMetaCampos campo : rutas){
            items.add(campo.getMc_nombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return v;

    }


}
