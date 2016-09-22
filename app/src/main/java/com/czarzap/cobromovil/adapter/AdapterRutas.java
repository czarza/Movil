package com.czarzap.cobromovil.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.menu.ListaComercioActivity;
import com.czarzap.cobromovil.rtprinter.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterRutas extends RecyclerView.Adapter<AdapterRutas.MyViewHolder> {

    private List<InMetaCampos> information;
    private Bundle args;

    public AdapterRutas(List<InMetaCampos> information, Bundle nombreRutas) {
        this.information = information;
        this.args = nombreRutas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_rutas, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final InMetaCampos current = information.get ( position );
        holder.ruta.setText (current.getMc_nombre ());
        final Bundle note = args;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                note.putString("id",current.getMc_campo ());
                Intent comercioIntent = new Intent(view.getContext (),ListaComercioActivity.class);
                comercioIntent.putExtras (note);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity)view.getContext (), view, "appcard");
                view.getContext ().startActivity(comercioIntent, options.toBundle());


            }
        });
    }

    @Override
    public int getItemCount() {
        return information.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ruta;

        public MyViewHolder(View itemView) {
            super ( itemView );
            ruta = (TextView) itemView.findViewById(R.id.tvRuta);
        }


    }
}

