package com.czarzap.cobromovil.search;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.adapter.AdapterEstablecido;
import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.utils.OfflineUtil;

import java.util.List;

import br.com.customsearchable.SearchActivity;
import br.com.customsearchable.contract.CustomSearchableConstants;
import br.com.customsearchable.model.CustomSearchableInfo;
import br.com.customsearchable.model.ResultItem;

public class BuscarContribuyente extends AppCompatActivity {
    private ProgressBar progress;
    private AdapterEstablecido adapter;
    private String url;
    private Integer empresa,numero;
    int onStartCount = 0;
    // Activity callbacks __________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        progress =(ProgressBar)findViewById(R.id.progress_barestablecido);
        final DatabaseManager manager = new DatabaseManager ( this );  // llamar a la Base
        url = manager.getWebService ( 1 );
        empresa = manager.getEmpresa();
        numero = manager.getAgente();
        CustomSearchableInfo.setTransparencyColor(Color.parseColor("#ccE3F2FD"));

        Intent intent = getIntent();
        handleIntent(intent);
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

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    // Handles the intent that carries user's choice in the Search Interface
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("Main", "Received query: " + query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                progress.setVisibility(View.VISIBLE);
                ResultItem receivedItem = bundle.getParcelable(CustomSearchableConstants.CLICKED_RESULT_ITEM);
                Integer id = Integer.valueOf(receivedItem.getSubHeader());

//                Log.i("RI.header", receivedItem.getHeader());
//                Log.i("RI.subHeader", receivedItem.getSubHeader());
//                Log.i("RI.leftIcon", receivedItem.getLeftIcon().toString());
//                Log.i("RI.rightIcon", receivedItem.getRightIcon().toString());
                loadJSON(id);
            }
        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_a_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvListEstablecido);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter (adapter);
        progress.setVisibility(View.GONE);
    }

    private void loadJSON(Integer id) {

        OfflineUtil util = new OfflineUtil();
        if(util.fileExistsComercio(getApplicationContext())){
            List<InComercios> comercios = util.getComercioxContribuyenteOffline(getApplicationContext(),id);
            adapter = new AdapterEstablecido (comercios);
            initViews ();
        }
    }
}
