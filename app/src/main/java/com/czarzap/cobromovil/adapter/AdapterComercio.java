package com.czarzap.cobromovil.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.datos.DatosSemiFijo;
import com.czarzap.cobromovil.rtprinter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class AdapterComercio extends RecyclerView.Adapter<AdapterComercio.MyViewHolder> {

    private List<InComercios> information;
    private Bundle map;


    public AdapterComercio(List<InComercios> information, Bundle map) {
        this.information = information;
        this.map=map;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_list_comercio, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final InComercios current = information.get (position);
       final String nombreRuta = map.getString(current.getCom_ruta());
        holder.propietario.setText(current.getCom_nombre_propietario());
        holder.domicilio.setText(current.getCom_ocupante());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("empresa",current.getCom_empresa().toString());
                args.putString("control",current.getCom_control().toString());
                args.putString("tipo",current.getCom_tipo());
                args.putString("contribuyente",current.getCom_contribuyente().toString());
                args.putString("ruta",nombreRuta);
                Intent comercioIntent = new Intent(view.getContext (), DatosSemiFijo.class);
                comercioIntent.putExtras (args);
                view.getContext().startActivity(comercioIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return information.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView propietario;
        TextView domicilio;
        public MyViewHolder(View itemView) {
            super ( itemView );
            propietario = (TextView) itemView.findViewById(R.id.tvPropietario);
            domicilio = (TextView) itemView.findViewById(R.id.tvDomicilio);
        }


    }

    public String tipoComercio(String tipo){
        String campo = null;
        switch(tipo){
            case "F": campo = "Fijo";
                break;
            case "S": campo = "Semi-Fijo";
                break;
            case "A": campo = "Ambulante";
                break;
            case "M": campo = "Moto-Taxis";
                break;
        }
        return campo;
    }
}
