package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercio_cobro_movil;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ReporteService {
    @FormUrlEncoded
    @POST("api/getCorte.htm")
    Call<List<InComercio_cobro_movil>> getCorte(@Field("empresa") Integer empresa,
                                                @Field("agente") Integer agente);

    @FormUrlEncoded
    @POST("api/getCorteTotal.htm")
    Call<BigDecimal> getTotal(@Field("empresa") Integer empresa,
                              @Field("agente") Integer agente);

}
