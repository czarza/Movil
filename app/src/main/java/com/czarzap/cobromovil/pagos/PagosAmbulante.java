package com.czarzap.cobromovil.pagos;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.czarzap.cobromovil.main.RTApplication;
import com.czarzap.cobromovil.utils.ToastUtil;
import com.dd.CircularProgressButton;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.service.CostoComercioService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import driver.BarcodeType;
import driver.Contants;
import driver.HsBluetoothPrintDriver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PagosAmbulante extends Activity {
        private static final int REQUEST_ENABLE_BT = 2;
        int onStartCount = 0;
        private final int handlerSign = 7173;

        Integer empresa, control,  contribuyente;
        Double tarifa;
        String tipo, propietario, quienOcupa, nombreEmpresa, domicilioEmpresa, rfcEmpresa;
        HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
        private Bitmap bmp;
        private EditText etCosto;
        private CircularProgressButton circularButton1, bImprimir,bRegresar;
        private EditText etNotas;
        private DatabaseManager manager;
        private InComercio_cobro_movil pagos;
        private CostoComercioService service;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_costo_comercio);
            onStartCount = 1;
            if (savedInstanceState == null) // 1st time
            {
                this.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            } else // already created so reverse animation
            {
                onStartCount = 2;
            }
            hsBluetoothPrintDriver.Begin();
            manager = new DatabaseManager(this);  // llamar a la Base
            initViews();

        }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }
        private void initViews() {

            empresa = getIntent().getExtras().getInt("empresa");
            control = getIntent().getExtras().getInt("control");                 // Leer los datos pasados por el QR
            contribuyente = getIntent().getExtras().getInt("contribuyente");
            tipo = getIntent().getExtras().getString("tipo");
            propietario = getIntent().getExtras().getString("propietario");
            quienOcupa = getIntent().getExtras().getString("quien");
            tarifa = getIntent().getExtras().getDouble("tarifa");
            etCosto = (EditText) findViewById(R.id.etCosto);
            etNotas = (EditText) findViewById(R.id.etNotas);
            circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
            bImprimir = (CircularProgressButton) findViewById(R.id.bImprimir);
            bRegresar = (CircularProgressButton) findViewById(R.id.bRegresar);
            if (tarifa != 0){
                etCosto.setText(tarifa.toString());
                etCosto.setEnabled(true);
            }
            if (RTApplication.getConnState() == Contants.UNCONNECTED) {
                ToastUtil.show(getApplicationContext(), "Impresora Desconectada");
                circularButton1.setEnabled(false);
            }
            nombreEmpresa = manager.getNombreEmpresa();
            domicilioEmpresa = manager.getDomicilioEmpresa();
            rfcEmpresa = manager.getRfceEmpresa();

            loadActivity();
        }

        private void loadActivity() {

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

            String url = manager.getWebService(1);                              // obtener el webService de Comercio
            Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(CostoComercioService.class);  // El servicio del Datos Comercio

            // Get local Bluetooth adapter
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth no esta Disponible", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            circularButton1.setIndeterminateProgressMode(true);
            circularButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    circularButton1.setProgress(50);        // Cargando
                    Double costo = Double.valueOf(etCosto.getText().toString());
                    if (required(etCosto)&&costo >= tarifa) {

                        String notas = etNotas.getText().toString();
                        if (notas.equalsIgnoreCase("")) notas = "";
                        Integer agente = manager.getAgente();    // Obtener el numero de agente

                        Call<InComercio_cobro_movil> call = service.setPagoAmbulante(empresa, control, contribuyente,tipo, new BigDecimal(costo), agente, notas);  // hace la llamada hacia SifiReceptoria

                        call.enqueue(new Callback<InComercio_cobro_movil>() {
                            @Override
                            public void onResponse(Call<InComercio_cobro_movil> call, Response<InComercio_cobro_movil> response) {
                                pagos = response.body();
                                    circularButton1.setProgress(100);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {bImprimir.setVisibility(CircularProgressButton.VISIBLE);}
                                    }, 800);

                            }
                            @Override
                            public void onFailure(Call<InComercio_cobro_movil> call, Throwable t) {
                                Log.d("Error", t.getMessage());circularButton1.setProgress(-1);}
                        });

                    }
                    if(costo<tarifa){
                        circularButton1.setProgress(-1);
                        Toast.makeText(getApplicationContext(), "No puede ser menor a la tarifa", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                circularButton1.setProgress(0);
                            }
                        }, 500);

                    }
                }
            });

            bImprimir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bRegresar.setVisibility(View.VISIBLE);
                    String agente = manager.getNombreAgente();       // obtener el nombre del Agente

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());  // Obtener el Logo
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File f = new File(directory, "logo.png");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ALPHA_8;
                    try { bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);} catch (FileNotFoundException e) {e.printStackTrace();}
                    String qr = "qr=2&contribuyente=" + contribuyente + "&control=" + control + "&pago=" + pagos.getCac_numero_pago();

                    printImage(bmp);
                    voidLine();
                    printTitle(nombreEmpresa);
                    printTitle("Tesoreria Municipal");
                    printLineCenter(domicilioEmpresa);
                    printLineCenter(rfcEmpresa);
                    voidLine();
                    printTitle("Recibo de Pago");
                    voidLine();
                    printLineBold("# Pago: " + pagos.getCac_numero_pago());
                    voidLine();
                    printLineBold("Comercio: " + tipoComercio(pagos.getCac_tipo()));
                    printLine("# Control: " + pagos.getCac_control());
                    printLine("Propietario: " + propietario);
                    printLine("Ocupante: " + quienOcupa);
                    printLine("Aportacion: " + pagos.getCac_total());
                    printLine("Fecha: " + pagos.getFecha_pago());
                    printLine("Agente: " + agente);
                    if (!pagos.getCac_notas().isEmpty()) printLine("Notas: " + pagos.getCac_notas());
                    printqr(qr);
                    lastLine();

//                Bitmap codeQR = encodeToQrCode(qr,100,100);
//                int newHeight = 600;
//                int newWidth = 600;
//                Bitmap bg1 = Bitmap.createScaledBitmap(codeQR, newWidth, newHeight, true);
//                printImage(bg1);

                }
            });

            bRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent comercioIntent = new Intent(PagosAmbulante.this, MenuActivity.class);
                    PagosAmbulante.this.startActivity(comercioIntent);
                }
            });
        }

        private void printLine(String line) {
            hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
            hsBluetoothPrintDriver.SetCharacterFont((byte) 0x01);
            hsBluetoothPrintDriver.SetAlignMode((byte) 0x10);
            hsBluetoothPrintDriver.SetBold((byte) 0x00);
            hsBluetoothPrintDriver.BT_Write(line);
            hsBluetoothPrintDriver.CR();

        }

        private void printLineCenter(String title) {
            hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
            hsBluetoothPrintDriver.SetCharacterFont((byte) 0x01);
            hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
            hsBluetoothPrintDriver.SetBold((byte) 0x00);
            hsBluetoothPrintDriver.BT_Write(title);
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.SelftestPrint();


        }

        private void printLineBold(String line) {
            hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
            hsBluetoothPrintDriver.SetCharacterFont((byte) 0x02);
            hsBluetoothPrintDriver.SetBold((byte) 0x01);
            hsBluetoothPrintDriver.BT_Write(line);
            hsBluetoothPrintDriver.CR();

        }

        private void printTitle(String line) {
            hsBluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
            hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
            hsBluetoothPrintDriver.SetBold((byte) 0x01);
            hsBluetoothPrintDriver.BT_Write(line);
            hsBluetoothPrintDriver.CR();

        }

        private void voidLine() {
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();
        }

        private void lastLine() {
            hsBluetoothPrintDriver.CR();
            hsBluetoothPrintDriver.LF();
            hsBluetoothPrintDriver.CR();
        }

        private void printImage(Bitmap bmp) {
            hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
            if (hsBluetoothPrintDriver.printImage(bmp, Contants.TYPE_58)) {
                hsBluetoothPrintDriver.LF();
            }
            hsBluetoothPrintDriver.StatusInquiryFinish(handlerSign, handler);
        }

        private void printqr(String line) {
            BarcodeType barcodeType;
            barcodeType = Enum.valueOf(BarcodeType.class, BarcodeType.QR_CODE.name());

            hsBluetoothPrintDriver.SetAlignMode((byte) 0x01);
            hsBluetoothPrintDriver.SetHRIPosition((byte) 0x02);
            hsBluetoothPrintDriver.AddCodePrint(barcodeType, line);

        }

//    public static Bitmap encodeToQrCode(String text, int width, int height){
//        QRCodeWriter writer = new QRCodeWriter();
//        BitMatrix matrix = null;
//        try {
//            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 100, 100);
//        } catch (WriterException ex) {
//            ex.printStackTrace();
//        }
//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        for (int x = 0; x < width; x++){
//            for (int y = 0; y < height; y++){
//                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
//            }
//        }
//        return bmp;
//    }


        public Boolean required(EditText editText) {
            String str = editText.getText().toString();
            if (str.equalsIgnoreCase("")) {
                editText.setError("Este campo es requerido");
                return false;
            }
            return true;
        }

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                switch (data.getInt("flag")) {
                    case handlerSign:
                        int state_a = data.getInt("state") & 0xFF;
                        if (state_a == 0x80) {
                            Log.d("handleM--------------", "finish");
                        }
                        break;
                }

            }
        };
    public String tipoComercio(String tipo){
        String campo = null;
        switch(tipo){
            case "F": campo = "Fijo";
                break;
            case "S": campo = "Semi-Fijo";
                break;
            case "A": campo = "Ambulante";
                break;
        }
        return campo;
    }
}
