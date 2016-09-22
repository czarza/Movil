package com.czarzap.cobromovil.utils;

import android.content.Context;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.Rutas;
import com.czarzap.cobromovil.search.Contribuyente;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Alfredo on 22/09/2016.
 */

public class OfflineUtil {
    static final private String contribuyentesJSON = "Contribuyentes.txt";
    static final private String comerciosJSON = "Comercios.txt";
    static final private String rutasJSON = "Rutas.txt";


    private void initDownloadComercios(List<InComercios> comercios,Context context){
        Gson gson = new Gson();
        String s = gson.toJson(comercios);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(comerciosJSON, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDownloadContribuyentes(List<Contribuyente> contribuyentes, Context context){
        Gson gson = new Gson();
        String s = gson.toJson(contribuyentes);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(contribuyentesJSON, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDownloadRutas(List<Rutas> rutas, Context context){
        Gson gson = new Gson();
        String s = gson.toJson(rutas);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(rutasJSON, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
