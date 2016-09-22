package com.czarzap.cobromovil.utils;

import android.content.Context;
import android.util.Log;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.Rutas;
import com.czarzap.cobromovil.search.Contribuyente;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alfredo on 22/09/2016.
 */

public class OfflineUtil {
    static final private String contribuyentesJSON = "Contribuyentes.txt";
    static final private String comerciosJSON = "Comercios.txt";
    static final private String rutasJSON = "Rutas.txt";


    public void initDownloadComercios(List<InComercios> comercios,Context context){
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

    public void initDownloadContribuyentes(List<Contribuyente> contribuyentes, Context context){
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

    public void initDownloadRutas(List<Rutas> rutas, Context context){
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

    public List<InComercios> comerciosData(Context context) throws IOException {

        FileInputStream fis = context.openFileInput(comerciosJSON);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<InComercios>>(){}.getType();
        List<InComercios> comercios = gson.fromJson(json, listType);
        for(InComercios c: comercios){
            Log.d("Comercios",c.getCom_control().toString());
        }
        return comercios;
    }


    public List<Contribuyente> contribuyentesData(Context context) throws IOException {

        FileInputStream fis = context.openFileInput(contribuyentesJSON);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Contribuyente>>(){}.getType();
        List<Contribuyente> contribuyentes = gson.fromJson(json, listType);
        for(Contribuyente c: contribuyentes){
            Log.d("Contribuyente",c.getNombre());
        }
        return contribuyentes;
    }


    public List<Rutas> rutasData(Context context) throws IOException {

        FileInputStream fis = context.openFileInput(rutasJSON);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Rutas>>(){}.getType();
        List<Rutas> rutas = gson.fromJson(json, listType);
        for(Rutas r: rutas){
            Log.d("Rutas",r.getNombre());
        }
        return rutas;
    }

}
