package com.czarzap.cobromovil.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.service.LoginService;
import com.czarzap.cobromovil.service.RegisterService;
import com.dd.CircularProgressButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetActivity extends AppCompatActivity {
    private EditText etPassowrd;
    private EditText etPassword2;
    private CircularProgressButton bCambiar;
    private RegisterService service;
    int onStartCount = 0;
    private DatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
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
        etPassowrd  = (EditText) findViewById(R.id.etPass);            // Parametrizar los datos que se ingresaron
        etPassword2 = (EditText) findViewById(R.id.etPass2);
        bCambiar = (CircularProgressButton) findViewById(R.id.bCambiar);
        manager = new DatabaseManager(this);            // Crear base de datos si no existe
        String url = manager.getWebService(1);
        Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RegisterService.class);  // El servicio del Login
        loadActivity();
    }

    private void loadActivity(){
        bCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(required(etPassowrd)&&required(etPassword2)&&compare(etPassowrd,etPassword2)){
                    String pass = etPassowrd.getText().toString();
                    Integer numero = manager.getAgente();
                    Integer empresa = manager.getEmpresa();
                    Call<String> call = service.setPass(empresa,numero,pass);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            bCambiar.setProgress(100);
                            Toast.makeText(getApplicationContext(), "Cambio de Contraseña guardado exitosamente", Toast.LENGTH_LONG).show();
                            Intent registerIntent = new Intent(ResetActivity.this,LoginActivity.class);
                            ResetActivity.this.startActivity(registerIntent);
                            bCambiar.setProgress(0);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            bCambiar.setProgress(-1);
                            Toast.makeText(getApplicationContext(), "Revise su conexion a Internet", Toast.LENGTH_LONG).show();
                            bCambiar.setProgress(0);
                        }
                    });


//                    String psw = etPassowrd.getText().toString();
//                    Integer numero = manager.getAgente();
//                    manager.setPassword(psw,numero);
                    Intent registerIntent = new Intent(ResetActivity.this,LoginActivity.class);
                    ResetActivity.this.startActivity(registerIntent);
                }
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

    public Boolean compare(EditText editText,EditText editText2) {
        String str = editText.getText().toString();
        String str2 = editText2.getText().toString();
        if (!str.equals(str2)) {
            editText.setError("Deben de ser iguales");
            return false;
        }
        return true;
    }


}
