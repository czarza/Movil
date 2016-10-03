package com.czarzap.cobromovil.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.beans.Rutas;
import com.czarzap.cobromovil.search.Contribuyente;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class OfflineUtil {
    static final private String contribuyentesJSON = "Contribuyentes.txt";
    static final private String comerciosJSON = "Comercios.txt";
    static final private String rutasJSON = "Rutas.txt";
    static final private String pagosJSON = "Pagos.txt";

    public void pagosSave(List<InComercio_cobro_movil> comercios,Context context){
        Gson gson = new Gson();
        String s = gson.toJson(comercios);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(pagosJSON, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean PagosDelete(Context context) throws FileNotFoundException {
        File file = context.getFileStreamPath(pagosJSON);
        if(file.delete()) {
            return false;
        }
        return true;
    }
    public Boolean ContribuyentesDelete(Context context) throws FileNotFoundException {
        File file = context.getFileStreamPath(contribuyentesJSON);
        if(file.delete()) {
            return false;
        }
        return true;
    }
    public Boolean ComerciosDelete(Context context) throws FileNotFoundException {
        File file = context.getFileStreamPath(comerciosJSON);
        if(file.delete()) {
            return false;
        }
        return true;
    }
    public Boolean RutasDelete(Context context) throws FileNotFoundException {
        File file = context.getFileStreamPath(rutasJSON);
        if(file.delete()) {
            return false;
        }
        return true;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public List<InComercio_cobro_movil> pagosData(Context context) throws IOException {

        FileInputStream fis = context.openFileInput(pagosJSON);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<InComercio_cobro_movil>>(){}.getType();
        List<InComercio_cobro_movil> pago = gson.fromJson(json, listType);
        for(InComercio_cobro_movil c: pago){
            Log.d("Comercios",c.getCac_numero_pago().toString());
        }
        return pago;
    }

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

    public void initDownloadRutas(List<InMetaCampos> rutas, Context context){
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


    public boolean fileExistsRuta(Context context) {
        File file = context.getFileStreamPath(rutasJSON);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public boolean fileExistsComercio(Context context) {
        File file = context.getFileStreamPath(comerciosJSON);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public boolean fileExistsContribuyente(Context context) {
        File file = context.getFileStreamPath(contribuyentesJSON);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public boolean fileExistPagos(Context context) {
        File file = context.getFileStreamPath(pagosJSON);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public List<InMetaCampos> rutasData(Context context) throws IOException {

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
        Type listType = new TypeToken<ArrayList<InMetaCampos>>(){}.getType();
        List<InMetaCampos> rutas = gson.fromJson(json, listType);
        for(InMetaCampos r: rutas){
            Log.d("Rutas",r.getMc_nombre());
        }
        return rutas;
    }

    public InComercios getComercioOffline(Context context,Integer control,String tipo) {
        List<InComercios> comercios = new ArrayList<>();
        try {
            comercios = this.comerciosData(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (InComercios c : comercios) {
            Integer tempControl = c.getCom_control();
            String tempTipo = c.getCom_tipo();
            if (control.equals(tempControl) && tipo.equals(tempTipo)) {
                return c;
            }

        }
        return null;
    }


    public List<InComercios> getComercioxContribuyenteOffline(Context context,Integer id) {
        List<InComercios> comercios = new ArrayList<>();
        List<InComercios> list = new ArrayList<>();
        try {
            comercios = this.comerciosData(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (InComercios c : comercios) {
            Integer contribuyente = c.getCom_contribuyente();
            if (id.equals(contribuyente)) {
                list.add(c);
                Log.d("Offline Comercio", c.toString());
            }

        }
        return list;
    }
}
