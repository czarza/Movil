package com.czarzap.cobromovil.comercio;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.adapter.AdapterSemiFijo;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.menu.BaseActivity;
import com.czarzap.cobromovil.service.DatosComercioService;
import com.czarzap.cobromovil.utils.OfflineUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaComercioActivity  extends BaseActivity {

    private AdapterSemiFijo adapter;
    private ProgressBar progress;
    int onStartCount = 0;
    private OfflineUtil util = new OfflineUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_lista_comercio );
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        progress=(ProgressBar)findViewById(R.id.progress_barcomercio);
        if(util.fileExistsComercio(getApplicationContext())){
            Log.d("Estado","Existe");
            try {
                String ruta = getIntent ().getExtras ().getString ( "id" );
                List<InComercios> comercios = util.comerciosData(getApplicationContext());
                List<InComercios> list = new ArrayList<>();
                for(InComercios c:comercios){
                    if(c.getCom_ruta() != null){
                        if(c.getCom_tipo().equals("S") && c.getCom_ruta().equals(ruta)){
                            list.add(c);
                        }
                    }
                }
                adapter = new AdapterSemiFijo(list);
                initViews ();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvListComercio);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter (adapter);
        progress.setVisibility(View.GONE);


    }

}
