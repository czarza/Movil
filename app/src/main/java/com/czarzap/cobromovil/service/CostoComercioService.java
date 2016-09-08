package com.czarzap.cobromovil.service;

import android.graphics.Bitmap;

import com.czarzap.cobromovil.beans.InComercio_cobro_movil;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface CostoComercioService {

    @FormUrlEncoded
    @POST("api/setPago.htm")
    Call<InComercio_cobro_movil> setPago(@Field("empresa") Integer empresa,
                                         @Field("control") Integer control,
                                         @Field("contribuyente") Integer contribuyente,
                                         @Field("tipo") String tipo,
                                         @Field("total") BigDecimal total,
                                         @Field("agente") Integer agente,
                                         @Field("notas") String notas,
                                         @Field("ruta") String ruta);

    @FormUrlEncoded
    @POST("api/getEmpresa.htm")
    Call<List<String>> getEmpresa(@Field("empresa") Integer empresa);

    @FormUrlEncoded
    @POST("api/setPagoAmbulante.htm")
    Call<InComercio_cobro_movil> setPagoAmbulante(@Field("empresa") Integer empresa,
                                         @Field("control") Integer control,
                                         @Field("contribuyente") Integer contribuyente,
                                         @Field("tipo") String tipo,
                                         @Field("total") BigDecimal total,
                                         @Field("agente") Integer agente,
                                         @Field("notas") String notas);

    @FormUrlEncoded
    @POST("api/getQr.htm")
    Call<ResponseBody> getQR(@Field("empresa") Integer empresa,
                             @Field("control") Integer control,
                             @Field("contribuyente") Integer contribuyente,
                             @Field("tipo") String tipo);
}
