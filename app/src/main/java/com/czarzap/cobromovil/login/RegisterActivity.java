package com.czarzap.cobromovil.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InAgentesMoviles;
import com.czarzap.cobromovil.beans.InEmpresas;
import com.czarzap.cobromovil.beans.InWebServices;
import com.czarzap.cobromovil.service.RegisterService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends Activity {

    private CircularProgressButton bRegister;
    private EditText etRFC;
    private EditText etNumberAgent;
    private EditText etNameAgent;
    private EditText etPassword;
    private EditText etCellPhone;
    private DatabaseManager manager;
    private Integer empresa;
    private InAgentesMoviles inAgentesMoviles;
    private InEmpresas empresas;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_register);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
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

    private void initViews(){



        manager = new DatabaseManager(this);            // Crear base de datos si no existe

        etRFC         = (EditText) findViewById(R.id.etRFC);            // Parametrizar los datos que se ingresaron
        etNumberAgent = (EditText) findViewById(R.id.etNumberAgent);
        etNameAgent   = (EditText) findViewById(R.id.etNameAgent);
        etPassword    = (EditText) findViewById(R.id.etPassword);
        etCellPhone   = (EditText) findViewById(R.id.etCellphone);
        bRegister     = (CircularProgressButton) findViewById(R.id.bRegister);
        loadActivity();
    }

    private void loadActivity(){
        bRegister.setIndeterminateProgressMode(true);
        bRegister.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            bRegister.setProgress(50);
                Handler handler;
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        inAgentesMoviles = new InAgentesMoviles();
                        inAgentesMoviles.setAm_numero(Integer.valueOf(etNumberAgent.getText().toString()));
                        inAgentesMoviles.setAm_nombre(etNameAgent.getText().toString());
                        inAgentesMoviles.setAm_cel(etCellPhone.getText().toString());
                        inAgentesMoviles.setAm_password(etPassword.getText().toString());
                        ObtenerWebService();
                    }
                }, 800);

            }
        });

    }

    private void getFolio(){
        String url = manager.getWebService(1);
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterService service;
        service = retrofit.create(RegisterService.class);
        Call<Integer> call = service.getFolio(empresa,inAgentesMoviles.getAm_numero());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!= null) manager.setLogin(response.body());
                else manager.setLogin(0);
                Bundle args = new Bundle();
                args.putInt("empresa",empresa);
                args.putInt("numero",inAgentesMoviles.getAm_numero());
                Intent registerIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                registerIntent.putExtras(args);
                RegisterActivity.this.startActivity(registerIntent);
                bRegister.setProgress(0);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error revise su conexion a Internet", Toast.LENGTH_LONG).show();
                System.out.print("ERROR OBTENER FOLIO");
            }
        });
    }



    private void ObtenerEmpresa(){
    String url = manager.getWebService(1);
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterService service;
        service = retrofit.create(RegisterService.class);
        String rfc = etRFC.getText().toString();
            Call<InEmpresas> call = service.getEmpresa(rfc);
            call.enqueue(new Callback<InEmpresas>() {
                @Override
                public void onResponse(Call<InEmpresas> call, Response<InEmpresas> response) {
                    empresas = response.body();
                    if(empresas != null){
                        bRegister.setProgress(100);
                        Toast.makeText(getApplicationContext(), "Registro Enviado Exitosamente", Toast.LENGTH_LONG).show();
                        inAgentesMoviles.setAm_empresa(empresa);
                        manager.insertarAgenteMovil(inAgentesMoviles);
                        manager.insertarEmpresa(empresas);
                        getFolio();
                    }
                    else{
                        bRegister.setProgress(-1);
                        Toast.makeText(getApplicationContext(), "No existe municipio registrado con ese RFC", Toast.LENGTH_LONG).show();
                        bRegister.setProgress(0);

                    }
                }

                @Override
                public void onFailure(Call<InEmpresas> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error revise su conexion a Internet", Toast.LENGTH_LONG).show();

                }
            });

    }

    public Boolean required(EditText editText) {
        String str = editText.getText().toString();
        if (str.equalsIgnoreCase("")) {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }

    private void ObtenerWebService(){
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
//                .baseUrl("https://www.sifi.com.mx:8443/SifiReceptoria/")
                .baseUrl("http://192.168.0.13:8081/SifiReceptoria/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterService service;
        service = retrofit.create(RegisterService.class);
        Call<List<InWebServices>> call = service.getWebServices(etRFC.getText().toString(),  // Manda los datos a SifiReceptoria
                inAgentesMoviles.getAm_numero(),
                inAgentesMoviles.getAm_nombre(),
                inAgentesMoviles.getAm_cel(),
                inAgentesMoviles.getAm_password(), new Date());

        call.enqueue(new Callback<List<InWebServices>>() {
            @Override
            public void onResponse(Call<List<InWebServices>> call, Response<List<InWebServices>> response) {   // En caso de que fue exitoso
                    List<InWebServices> webServices = response.body();   // Obtener la respuesta y castearlo a una Clase
                if(!webServices.isEmpty()){
                    for(InWebServices webService: webServices){
                        empresa = webService.getWs_empresa();
                        manager.insertarWebService(webService);                      // Insertar la lista de  WebServices en la base de datos
                    }
                    ObtenerEmpresa();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error, revise el RFC Ingresado", Toast.LENGTH_LONG).show();
                    bRegister.setProgress(0);
                }

            }

            @Override 
            public void onFailure(Call<List<InWebServices>> call, Throwable t) {              // En caso de haber un error
                bRegister.setProgress(-1);
                Toast.makeText(getApplicationContext(), "Error, revise su conexion a Internet", Toast.LENGTH_LONG).show();
                bRegister.setProgress(0);
            }
        });
    }

}
