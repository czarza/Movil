package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.search.Contribuyente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by carloszarza on 10/5/16.
 */

public interface AgregarService {

    @FormUrlEncoded
    @POST("api/agregarContribuyente.htm")
    Call<Contribuyente> agregarContribuyente(@Field("empresa") Integer empresa,
                                             @Field("nombre") String nombre,
                                             @Field("domicilio") String domicilio);

    @POST("api/agregarComercio.htm")
    Call<InComercios> agregarComercio(@Body InComercios comercio_nofijo);

}
