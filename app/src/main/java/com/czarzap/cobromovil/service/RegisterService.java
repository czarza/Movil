package com.czarzap.cobromovil.service;
import com.czarzap.cobromovil.beans.InEmpresas;
import com.czarzap.cobromovil.beans.InWebServices;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {

    @FormUrlEncoded
    @POST("api/register.htm")
    Call<List<InWebServices>> getWebServices(@Field("rfc") String rfc,
                                             @Field("numero") Integer numero,
                                             @Field("nombre") String nombre,
                                             @Field("telefono") String telefono,
                                             @Field("password") String password,
                                             @Field("alta") Date alta);


    @FormUrlEncoded
    @POST("api/getEmpresa.htm")
    Call<InEmpresas> getEmpresa(@Field("rfc") String rfc);

    @FormUrlEncoded
    @POST("api/pass.htm")
    Call<String> setPass(@Field("empresa") Integer empresa,
                         @Field("numero") Integer numero,
                         @Field("pass") String pass);

    @FormUrlEncoded
    @POST("api/getFolio.htm")
    Call<Integer> getFolio(@Field("empresa") Integer empresa,
                         @Field("numero") Integer numero);
}
