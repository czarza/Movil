package com.czarzap.cobromovil.utils;


import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.content.Context;
import android.content.ContextWrapper;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.beans.InComercios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import driver.BarcodeType;
import driver.Contants;
import driver.HsBluetoothPrintDriver;

public class Util {

    public void printEstadoCuenta( InComercios comercio, Context context, HsBluetoothPrintDriver hsBluetoothPrintDriver){
        Bitmap bmp = null;
        DatabaseManager manager = new DatabaseManager(context);
        String nombre = manager.getNombreEmpresa();
        String domicilio = manager.getDomicilioEmpresa();
        String rfc = manager.getRfceEmpresa();
        ContextWrapper cw = new ContextWrapper(context);  // Obtener el Logo
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "logo.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        try { bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);} catch (FileNotFoundException e) {e.printStackTrace();}
        printImage(bmp,hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printTitle(nombre,hsBluetoothPrintDriver);
        printTitle("Tesoreria Municipal",hsBluetoothPrintDriver);
        printLineCenter(domicilio,hsBluetoothPrintDriver);
        printLineCenter(rfc,hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printTitle("Estado de Cuenta",hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printLine("# Control: " + comercio.getCom_control(),hsBluetoothPrintDriver);
        printLine("Propietario: " + comercio.getCom_nombre_propietario(),hsBluetoothPrintDriver);
        printLine("Ocupante: " + comercio.getCom_ocupante(),hsBluetoothPrintDriver);
        printLineBold("Ultima Licencia: " + comercio.getCom_ult_eje(),hsBluetoothPrintDriver);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = new Date();
        printLine("Fecha de Aviso: " + dateFormat.format(date),hsBluetoothPrintDriver);
        printLine("Agente: " +  manager.getNombreAgente(),hsBluetoothPrintDriver);
        lastLine(hsBluetoothPrintDriver);
    }

    public void printEstadoCuentaMoto( InComercios comercio, Context context, HsBluetoothPrintDriver hsBluetoothPrintDriver){
        Bitmap bmp = null;
        DatabaseManager manager = new DatabaseManager(context);
        String nombre = manager.getNombreEmpresa();
        String domicilio = manager.getDomicilioEmpresa();
        String rfc = manager.getRfceEmpresa();
        ContextWrapper cw = new ContextWrapper(context);  // Obtener el Logo
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "logo.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        try { bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);} catch (FileNotFoundException e) {e.printStackTrace();}
        printImage(bmp,hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printTitle(nombre,hsBluetoothPrintDriver);
        printTitle("Tesoreria Municipal",hsBluetoothPrintDriver);
        printLineCenter(domicilio,hsBluetoothPrintDriver);
        printLineCenter(rfc,hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printTitle("Estado de Cuenta",hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printLine("# Control: " + comercio.getCom_control(),hsBluetoothPrintDriver);
        printLine("Propietario: " + comercio.getCom_nombre_propietario(),hsBluetoothPrintDriver);
        printLine("Ocupante: " + comercio.getCom_ocupante(),hsBluetoothPrintDriver);
        printLineBold("Placa: " + comercio.getCom_num_placa(),hsBluetoothPrintDriver);
        printLine("# Tarjeta: " + comercio.getCom_num_tarj_circulacion(),hsBluetoothPrintDriver);
        printLineBold("Ultima Licencia: " + comercio.getCom_ult_eje(),hsBluetoothPrintDriver);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = new Date();
        printLine("Fecha de Aviso: " + dateFormat.format(date),hsBluetoothPrintDriver);
        printLine("Agente: " +  manager.getNombreAgente(),hsBluetoothPrintDriver);
        lastLine(hsBluetoothPrintDriver);
    }

    public void pago(InComercio_cobro_movil pagos, Context context, HsBluetoothPrintDriver hsBluetoothPrintDriver,
                     String nombreEmpresa, String domicilioEmpresa, String rfcEmpresa, String agente,
                     InComercios comercio){
        Bitmap bmp = null;
        ContextWrapper cw = new ContextWrapper(context);  // Obtener el Logo
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "logo.png");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        try { bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);} catch (FileNotFoundException e) {e.printStackTrace();}
        String qr = "qr=2&contribuyente=" + comercio.getCom_contribuyente() + "&control=" + comercio.getCom_control()
                + "&pago=" + pagos.getCac_numero_pago()+ "&empresa=" + comercio.getCom_empresa() + "&tipo=" + comercio.getCom_tipo();

        if(bmp!= null){
            printImage(bmp,hsBluetoothPrintDriver);
            voidLine(hsBluetoothPrintDriver);
        }
        printTitle(nombreEmpresa,hsBluetoothPrintDriver);
        printTitle("Tesoreria Municipal",hsBluetoothPrintDriver);
        printLineCenter(domicilioEmpresa,hsBluetoothPrintDriver);
        printLineCenter(rfcEmpresa,hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printTitle("Recibo de Pago",hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printLineBold("# Pago: " + pagos.getCac_numero_pago(),hsBluetoothPrintDriver);
        voidLine(hsBluetoothPrintDriver);
        printLineBold("Comercio: " + tipoComercio(comercio.getCom_tipo()),hsBluetoothPrintDriver);
        printLine("# Control: " + comercio.getCom_control(),hsBluetoothPrintDriver);
        printLine("Ruta: " + comercio.getNombreRuta(),hsBluetoothPrintDriver);
        printLine("Propietario: " + comercio.getCom_nombre_propietario(),hsBluetoothPrintDriver);
        printLine("Ocupante: " + comercio.getCom_ocupante(),hsBluetoothPrintDriver);
        printLine("Aportacion: " + pagos.getCac_total(),hsBluetoothPrintDriver);
        printLine("Fecha: " + pagos.getFecha_pago(),hsBluetoothPrintDriver);
        printLine("Agente: " + agente,hsBluetoothPrintDriver);
        if (!pagos.getCac_notas().isEmpty()) printLine("Notas: " + pagos.getCac_notas(),hsBluetoothPrintDriver);
        printqr(qr,hsBluetoothPrintDriver);
        lastLine(hsBluetoothPrintDriver);
    }


    public String tipoComercio(String tipo){
        String campo = null;
        if (tipo.equals("F")) {
            campo = "Fijo";

        } else if (tipo.equals("S")) {
            campo = "Semi-Fijo";

        } else if (tipo.equals("A")) {
            campo = "Ambulante";

        }
        return campo;
    }


    private void printLine(String line, HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
        hsBluetoothPrintDriver.SetCharacterFont((byte) 0x01);
        hsBluetoothPrintDriver.SetAlignMode((byte) 0x10);
        hsBluetoothPrintDriver.SetBold((byte) 0x00);
        hsBluetoothPrintDriver.BT_Write(line);
        hsBluetoothPrintDriver.CR();

    }

    private void printLineCenter(String title,HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
        hsBluetoothPrintDriver.SetCharacterFont((byte) 0x01);
        hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
        hsBluetoothPrintDriver.SetBold((byte) 0x00);
        hsBluetoothPrintDriver.BT_Write(title);
        hsBluetoothPrintDriver.CR();


    }
    private void printLineBold(String line,HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
        hsBluetoothPrintDriver.SetCharacterFont((byte) 0x02);
        hsBluetoothPrintDriver.SetBold((byte) 0x01);
        hsBluetoothPrintDriver.BT_Write(line);
        hsBluetoothPrintDriver.CR();

    }

    private void printTitle(String line,HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
        hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
        hsBluetoothPrintDriver.SetBold((byte) 0x01);
        hsBluetoothPrintDriver.BT_Write(line);
        hsBluetoothPrintDriver.CR();

    }

    private void voidLine(HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.CR();
        hsBluetoothPrintDriver.LF();
    }

    private void lastLine(HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.CR();
        hsBluetoothPrintDriver.LF();
        hsBluetoothPrintDriver.CR();
    }
    private final int handlerSign = 7173;
    private int state_a = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            switch (data.getInt("flag")) {
                case handlerSign:
                    state_a =data.getInt("state") & 0xFF;
                    if (state_a == 0x80) {
                        Log.d("handleM--------------", "finish");
                    }
                    break;
            }
        }
    };
    private void printImage(Bitmap bmp,HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);

        if (hsBluetoothPrintDriver.printImage(bmp, Contants.TYPE_58)) {
            hsBluetoothPrintDriver.LF();
        }
        hsBluetoothPrintDriver.StatusInquiryFinish(handlerSign, handler);
    }

    private void printqr(String line,HsBluetoothPrintDriver hsBluetoothPrintDriver) {
        BarcodeType barcodeType;
        barcodeType = Enum.valueOf(BarcodeType.class, BarcodeType.QR_CODE.name());

        hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
        hsBluetoothPrintDriver.SetHRIPosition((byte) 0x02);
        hsBluetoothPrintDriver.AddCodePrint(barcodeType, line);

    }

    public Boolean required(EditText editText){
        String  str = editText.getText().toString();
        if(str.equalsIgnoreCase(""))
        {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }

}
