package com.czarzap.cobromovil.login;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InAgentesMoviles;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.service.LoginService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private DatabaseManager manager;
    private TextView registerLink;
    private CircularProgressButton bLogin;
    private EditText etPassword;
    private LoginService service;
    private Integer count,numeroAgente;
    private String pass,status;
    private InAgentesMoviles agente;
    private CheckBox check;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = new DatabaseManager(this);            // Crear base de datos si no existe
        if(manager.getLogin()!= null){
            if(manager.getLogin().equals("true")){
                Intent registerIntent = new Intent(LoginActivity.this, MenuActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        }
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }

        String url = manager.getWebService(1); // obtener el webService de login
        if(url!=null){
            Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(LoginService.class);  // El servicio del Login
        }
       initValues();

    }


    private void setcheck(){
        if(check.isChecked()){
            manager.login();
        }
        else{
            manager.logout();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        manager = new DatabaseManager(this);            // Crear base de datos si no existe
        String url = manager.getWebService(1); // obtener el webService de login
        if(url!=null){
            Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(LoginService.class);  // El servicio del Login
        }
        count = manager.existeAgente();

    }


    private void initValues(){
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (CircularProgressButton) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegister);
        check = (CheckBox) findViewById(R.id.checkBoxLogin);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcheck();
            }
        });
        existeRegistro();
    }

    private void existeRegistro(){
        count = manager.existeAgente();
        if (count == 0){ registerLink.setVisibility(TextView.VISIBLE);      // Si no se ha registrado, pone visible el link de registro
                         check.setEnabled(false);     }
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                onDestroy();
            }
        });

        loadView();
    }


    private void loadView(){

        bLogin.setIndeterminateProgressMode(true);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (required(etPassword)){
                    pass = etPassword.getText().toString();
                    getStatusAgente();
                }

            }
        });

       new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    private void getStatusAgente(){
        bLogin.setProgress(50); // cargando

        if (count == 0) {
            bLogin.setProgress(-1);
            Toast.makeText(getApplicationContext(), "No existe registro en el Dispositivo", Toast.LENGTH_LONG).show();
            bLogin.setProgress(0);
        }
        else {
            Integer empresa = manager.getEmpresa();
            numeroAgente = manager.getAgente();
            if (empresa == null) empresa = getIntent().getExtras().getInt("empresa");
            if (numeroAgente == null) numeroAgente = getIntent().getExtras().getInt("numero");
            Integer pago = manager.getFolio();
            Call<InAgentesMoviles> call = service.getAgente(empresa, numeroAgente, pass,pago);
            call.enqueue(new Callback<InAgentesMoviles>() {
                @Override
                public void onResponse(Call<InAgentesMoviles> call, Response<InAgentesMoviles> response) {
                    agente = response.body();
                    status = agente.getAm_status();
                    manager.updateAgente(status,numeroAgente);
                    Handler handler;
                    if(status == null){
                        bLogin.setProgress(-1);
                        Toast.makeText(getApplicationContext(), "Contraseña Incorrecta", Toast.LENGTH_LONG).show();
                        bLogin.setProgress(0);
                    }
                    else {
                        switch (status) {
                            case "A":
                                if(agente.getAm_password().equals(etPassword.getText().toString())){
                                    bLogin.setProgress(100);
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
                                            LoginActivity.this.startActivity(menuIntent);
                                            bLogin.setProgress(0);
                                        }
                                    }, 800);
                                }
                               else{
                                    bLogin.setProgress(-1);
                                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                    bLogin.setProgress(0);
                                }
                                break;
                            case "P":
                                bLogin.setProgress(-1);
                                Toast.makeText(getApplicationContext(), "El Administrador aun no ha aprobado su registro", Toast.LENGTH_LONG).show();
                                bLogin.setProgress(0);
                                break;
                            case "B":
                                bLogin.setProgress(-1);
                                Toast.makeText(getApplicationContext(), "Acceso Denegado", Toast.LENGTH_LONG).show();
                                bLogin.setProgress(0);
                                break;
                            case "R":
                                bLogin.setProgress(100);
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        Intent menuIntent = new Intent(LoginActivity.this, ResetActivity.class);
                                        LoginActivity.this.startActivity(menuIntent);
                                        bLogin.setProgress(0);
                                    }
                                }, 800);
                                break;

                        }
                    }
                }

                @Override
                public void onFailure(Call<InAgentesMoviles> call, Throwable t) {
                    String status = manager.getStatus();
                    String pass = manager.get();
                    if(status != null) {
                        if (status.equals("A") && pass.equals(etPassword.getText().toString())) {
                            Handler handler;
                            bLogin.setProgress(100);
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
                                    LoginActivity.this.startActivity(menuIntent);
                                    bLogin.setProgress(0);
                                }
                            }, 800);
                        }
                    }
                }
            });
        }

    }

    public Boolean required(EditText editText) {
        String str = editText.getText().toString();
        if (str.equalsIgnoreCase("")) {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }


}
