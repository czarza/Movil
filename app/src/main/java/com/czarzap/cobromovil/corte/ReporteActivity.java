package com.czarzap.cobromovil.corte;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.adapter.AdapterPagos;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.service.ReporteService;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReporteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private AdapterPagos adapter;
    private ProgressBar progress;
    private ReporteService service;
    private BigDecimal total;
    private Integer numero,empresa;
    private List<InComercio_cobro_movil> comerciosPagos;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        onStartCount = 1;
        if (savedInstanceState == null)this.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
        else onStartCount = 2;

        final DatabaseManager manager = new DatabaseManager(this);  // llamar a la Base
        String url = manager.getWebService(1);
        numero = manager.getAgente();
        empresa = manager.getEmpresa();
        loadJSON(url);
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
        recyclerView = (RecyclerView)findViewById(R.id.rvListPagos);
        progress=(ProgressBar)findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapter = new AdapterPagos (comerciosPagos);
                recyclerView.setAdapter (adapter );
                progress.setVisibility(View.GONE);

            }
        }, 800);


    }



    private void loadJSON(String url) {

        Retrofit retrofit = new Retrofit.Builder ()                          // Crear REST
                .baseUrl ( url )
                .addConverterFactory ( GsonConverterFactory.create () )
                .build ();

        service = retrofit.create ( ReporteService.class );  // El servicio del Datos
        Call<List<InComercio_cobro_movil>> call = service.getCorte (empresa,numero);
        call.enqueue(new Callback<List<InComercio_cobro_movil>>() {
            @Override
            public void onResponse(Call<List<InComercio_cobro_movil>> call, Response<List<InComercio_cobro_movil>> response) {
                comerciosPagos = response.body();
                loadTotal();
                initViews();
            }

            @Override
            public void onFailure(Call<List<InComercio_cobro_movil>> call, Throwable t) {

            }
        });

    }

    private void loadTotal(){

        Call<BigDecimal> call = service.getTotal(empresa,numero);
        call.enqueue(new Callback<BigDecimal>() {
            @Override
            public void onResponse(Call<BigDecimal> call, Response<BigDecimal> response) {
                tvTotal = (TextView)findViewById(R.id.footer);
                total = response.body();
                tvTotal.setText("$ "+total.toString());
            }

            @Override
            public void onFailure(Call<BigDecimal> call, Throwable t) {

            }
        });
    }

}
