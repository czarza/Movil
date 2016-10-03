package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercios;
import com.czarzap.cobromovil.beans.InMetaCampos;
import com.czarzap.cobromovil.search.Contribuyente;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<List<InMetaCampos>> downloadRutas(@Field("empresa") Integer empresa);

    @POST("api/carga.htm")
    Call<String> upload(@Body String pagos);

    @FormUrlEncoded
    @POST("api/getLogo.htm")
    Call<ResponseBody> getLogo(@Field("empresa") Integer empresa);

    @FormUrlEncoded
    @POST("api/folio.htm")
    Call<String> setFolio(@Field("empresa") Integer empresa, @Field("numero") Integer numero, @Field("folio") Integer folio);

    @FormUrlEncoded
    @POST("api/test.htm")
    Call<String> test(@Field("empresa") Integer empresa);

    @FormUrlEncoded
    @POST("api/testRutas.htm")
    Call<String> testRutas(@Field("empresa") Integer empresa, @Field("numero") Integer numero);

    @FormUrlEncoded
    @POST("api/testComercios.htm")
    Call<String> testComercios(@Field("empresa") Integer empresa, @Field("numero") Integer numero);

}
