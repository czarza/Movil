package com.czarzap.cobromovil.adapter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czarzap.cobromovil.beans.InComercio_cobro_movil;
import com.czarzap.cobromovil.datos.DatosSemiFijo;
import com.czarzap.cobromovil.pagos.VistaPagoSemiFijo;
import com.czarzap.cobromovil.rtprinter.R;

import java.io.Serializable;
import java.util.List;

public class AdapterPagos extends RecyclerView.Adapter<AdapterPagos.MyViewHolder> {
    private List<InComercio_cobro_movil> listaPagos;

    public AdapterPagos(List<InComercio_cobro_movil> listaPagos) {
        this.listaPagos = listaPagos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_pagos, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final InComercio_cobro_movil temp = listaPagos.get ( position );
        String numeroPago = temp.getCac_numero_pago().toString();
        String propietario = temp.getPropietario();
        String monto = temp.getCac_total().toString();
        String tipo = temp.getCac_tipo();
        String sub = " # Pago: "+ numeroPago + "  "+ tipoComercio(tipo);
        String total = "$"+monto;

        holder.titulo.setText (propietario);
        holder.subTitulo.setText(sub);
        holder.total.setText(total);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext (), VistaPagoSemiFijo.class);
                intent.putExtra("comercio",temp);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaPagos.size ();
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

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView subTitulo;
        TextView total;

        public MyViewHolder(View itemView) {
            super ( itemView );
            titulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            subTitulo = (TextView) itemView.findViewById(R.id.tvSubTitulo);
            total = (TextView) itemView.findViewById(R.id.tvTotal);
        }


    }
}
