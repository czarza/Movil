package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.Rutas;
import com.czarzap.cobromovil.search.Contribuyente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public interface OfflineService {

    @FormUrlEncoded
    @POST("api/descargaComercios.htm")
    Call<List<InComercios>> downloadComercios(@Field("empresa") Integer empresa, @Field("numero") Integer numero);

    @FormUrlEncoded
    @POST("api/descargaContribuyentes.htm")
    Call<List<Contribuyente>> downloadContribuyentes(@Field("empresa") Integer empresa, @Field("numero") Integer numero);

    @FormUrlEncoded
    @POST("api/descargaRutas.htm")
    Call<List<Rutas>> downloadRutas(@Field("empresa") Integer empresa);




    @FormUrlEncoded
    @POST("api/carga.htm")
    Call<InComercios> upload(@Field("empresa") Integer empresa);
}
