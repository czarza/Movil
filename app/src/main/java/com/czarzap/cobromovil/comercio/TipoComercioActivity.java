package com.czarzap.cobromovil.comercio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.corte.ReporteActivity;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.add.Agregar;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.search.BuscarContribuyente;
import com.czarzap.cobromovil.utils.OfflineUtil;


public class TipoComercioActivity extends BaseActivity {
    private Button bAdd;
    private Button bSemifijo;
    private Button bFijo;
    private Button bCorte;
    private Button bPagos;
    private Integer empresa;
    int onStartCount = 0;
    OfflineUtil util = new OfflineUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Comercio");
        setContentView( R.layout.activity_tipo_comercio);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        initView();
    }

    private void initView(){
        DatabaseManager manager = new DatabaseManager(this);
        empresa = manager.getEmpresa();
        bAdd = (Button) findViewById(R.id.bAdd);
        bFijo = (Button) findViewById(R.id.bEstablecido);
        bSemifijo     = (Button) findViewById(R.id.bSemiFijo);
        bCorte = (Button) findViewById(R.id.bCorte);
        bPagos = (Button) findViewById(R.id.bPagos);
        bAdd.setEnabled(false);
        loadActivity();
    }

    private void loadActivity(){
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoComercioActivity.this,Agregar.class);
                TipoComercioActivity.this.startActivity(intent);
                TipoComercioActivity.this.finish();
            }
        });
        bFijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean file = util.fileExistsContribuyente(getApplicationContext());
                if(!file){
                    bFijo.setEnabled(false);
                }
                if(bFijo.isEnabled()){
                    Bundle args = new Bundle();
                    Intent intent = new Intent(TipoComercioActivity.this,BuscarContribuyente.class);
                    args.putString("buscar","S");
                    intent.putExtras(args);
                    TipoComercioActivity.this.startActivity(intent);
                    TipoComercioActivity.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No existen contribuyentes, descargue el padron", Toast.LENGTH_LONG).show();
                }
                bFijo.setEnabled(true);
            }
        });
        bSemifijo.setOnClickListener(new View.OnClickListener() {  // Click Listener Qr
            @Override
            public void onClick(View v) {
                Boolean file = util.fileExistsRuta(getApplicationContext());
                if(!file){
                    bSemifijo.setEnabled(false);
                }
                if(bSemifijo.isEnabled()){
                    Bundle args = new Bundle();
                    args.putInt("empresa",empresa);
                    Intent comercioIntent = new Intent(TipoComercioActivity.this,ListaRutasActivity.class);
                    comercioIntent.putExtras ( args );
                    TipoComercioActivity.this.startActivity(comercioIntent);
                    TipoComercioActivity.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No existen rutas, descargue el padron", Toast.LENGTH_LONG).show();
                }
                bSemifijo.setEnabled(true);

            }


        });
        bPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean file = util.fileExistPagos(getApplicationContext());
                if(!file){
                    bPagos.setEnabled(false);
                }
                if(bPagos.isEnabled()){
                    Bundle args = new Bundle();
                    args.putInt("empresa",empresa);
                    Intent comercioIntent = new Intent(TipoComercioActivity.this,PagosActivity.class);
                    comercioIntent.putExtras ( args );
                    TipoComercioActivity.this.startActivity(comercioIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No hay Pagos", Toast.LENGTH_LONG).show();
                }
                bPagos.setEnabled(true);
            }
        });
        bCorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean file = util.fileExistPagos(getApplicationContext());
                if(file){
                    bCorte.setEnabled(false);
                }
                if(bCorte.isEnabled()){
                    Bundle args = new Bundle();
                    args.putInt("empresa",empresa);
                    Intent comercioIntent = new Intent(TipoComercioActivity.this,ReporteActivity.class);
                    comercioIntent.putExtras ( args );
                    TipoComercioActivity.this.startActivity(comercioIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Acceso Denegado, existen pagos sin sincronizar", Toast.LENGTH_LONG).show();
                }
                bCorte.setEnabled(true);

            }
        });

    }

}