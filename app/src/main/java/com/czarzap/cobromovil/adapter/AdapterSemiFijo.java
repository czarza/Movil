package com.czarzap.cobromovil.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.datos.DatosAmbulante;
import com.czarzap.cobromovil.datos.DatosEstablecido;
import com.czarzap.cobromovil.datos.DatosMotos;
import com.czarzap.cobromovil.datos.DatosSemiFijo;
import com.czarzap.cobromovil.rtprinter.R;

import java.util.List;

public class AdapterSemiFijo extends RecyclerView.Adapter<AdapterSemiFijo.MyViewHolder> {

    private List<InComercios> information;
    private String tipo;
    private Bundle args;
    public AdapterSemiFijo(List<InComercios> information) {
        this.information = information;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_list_comercio, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final InComercios current = information.get(position);

        if(current.getPagoHoy()!= null){
            if(current.getPagoHoy().equals(true)) holder.ok.setVisibility(View.VISIBLE);
        }
        String giro = current.getGiros();
        tipo = current.getCom_tipo();
        if(giro == null) giro = "";

        holder.quienOcupa.setText("# " +current.getCom_control() + " - " + current.getCom_nombre_propietario());
        holder.domicilio.setText(current.getCom_domicilio() + " - "+giro);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                args = new Bundle();
                args.putString("empresa",current.getCom_empresa().toString());
                args.putString("control",current.getCom_control().toString());
                args.putString("tipo",tipo);
                args.putString("contribuyente",current.getCom_contribuyente().toString());
                args.putString("ruta",current.getCom_ruta());
                swicth(tipo,view);

            }

            private void swicth(String tipo, View view) {
                Intent intent;
                switch (tipo){
                    case "S":
                        intent = new Intent(view.getContext (), DatosSemiFijo.class);
                        intent.putExtras (args);
                        view.getContext().startActivity(intent);
                        break;
                    case "F":
                        intent = new Intent(view.getContext (), DatosEstablecido.class);
                        intent.putExtras (args);
                        view.getContext().startActivity(intent);
                        break;
                    case "A":
                        intent = new Intent(view.getContext(), DatosAmbulante.class);
                        intent.putExtras (args);
                        view.getContext().startActivity(intent);
                        break;
                    case "M":
                        intent = new Intent(view.getContext(), DatosMotos.class);
                        intent.putExtras (args);
                        view.getContext().startActivity(intent);
                        break;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return information.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quienOcupa;
        TextView domicilio;
        ImageView ok;
        public MyViewHolder(View itemView) {
            super ( itemView );
            quienOcupa = (TextView) itemView.findViewById(R.id.tvPropietario);
            domicilio = (TextView) itemView.findViewById(R.id.tvDomicilio);
            ok = (ImageView) itemView.findViewById(R.id.ok);
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