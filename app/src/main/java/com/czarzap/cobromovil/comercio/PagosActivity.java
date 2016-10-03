package com.czarzap.cobromovil.comercio;

import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.adapter.AdapterPagos;
import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.utils.OfflineUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


public class PagosActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private AdapterPagos adapter;
    private ProgressBar progress;
    private List<InComercio_cobro_movil> comerciosPagos;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Pagos Generados");
        setContentView(R.layout.activity_reporte);

        onStartCount = 1;
        if (savedInstanceState == null)this.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
        else onStartCount = 2;

        try {
            loadJSON();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void loadJSON() throws IOException {
        OfflineUtil util = new OfflineUtil();
        comerciosPagos = util.pagosData(getApplicationContext());
        BigDecimal temp = new BigDecimal(0);
        for(InComercio_cobro_movil c: comerciosPagos){
            temp = temp.add(c.getCac_total());
        }
        tvTotal = (TextView)findViewById(R.id.footer);
        String total = "$ "+ temp;
        tvTotal.setText(total);

        initViews();
    }

}
