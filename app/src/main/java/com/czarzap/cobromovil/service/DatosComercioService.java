package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.search.Contribuyente;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DatosComercioService {

    @FormUrlEncoded
    @POST("api/getComercio.htm")
    Call<InComercios> getComercio(@Field("empresa") Integer empresa,
                                  @Field("control") Integer control,
                                  @Field("tipo") String tipo);


    @POST("api/setComercio.htm")
    Call<InComercios> setComercio(@Body InComercios comercio_nofijo);

    @FormUrlEncoded
    @POST("api/getListRutas.htm")
    Call<List<InMetaCampos>> getListRutas(@Field("empresa") Integer empresa);


    @FormUrlEncoded
    @POST("api/getListComercio.htm")
    Call<List<InComercios>> getListComerciosSemiFijo(@Field("ruta") String ruta,
                                                     @Field("empresa") Integer empresa);

    @POST("api/getListAmbulantes.htm")
    Call<List<InComercios>> getListAmbulantes();

    @FormUrlEncoded
    @POST("api/getTarifa.htm")
    Call<BigDecimal> getTarifa(@Field("empresa") Integer empresa,
                               @Field("tipo") String tipo,
                               @Field("local") String local);

    @FormUrlEncoded
    @POST("api/getContribuyentes.htm")
    Call<List<Contribuyente>> getContribuyentes(@Field("empresa") Integer empresa);

    @FormUrlEncoded
    @POST("api/getComercioxContribuyente.htm")
    Call<List<InComercios>> getComercios(@Field("empresa") Integer empresa,
                                         @Field("contribuyente") Integer contribuyente,
                                         @Field("numero") Integer numero);

    @FormUrlEncoded
    @POST("api/existeImagen.htm")
    Call<Boolean> existeImagen(@Field("empresa") Integer empresa,
                                  @Field("control") Integer control,
                                  @Field("tipo") String tipo);
}
