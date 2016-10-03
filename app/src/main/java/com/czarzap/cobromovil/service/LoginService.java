package com.czarzap.cobromovil.service;


import com.czarzap.cobromovil.beans.InAgentesMoviles;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface LoginService {

    @FormUrlEncoded
    @POST("api/login.htm")
    Call<InAgentesMoviles> getAgente(@Field("empresa") Integer empresa,
                                     @Field("numero") Integer numero,
                                     @Field("pass") String password,
                                     @Field("pago") Integer pago);






}
