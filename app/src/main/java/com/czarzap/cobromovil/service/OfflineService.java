package com.czarzap.cobromovil.service;

import com.czarzap.cobromovil.beans.InComercios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public interface OfflineService {

    @FormUrlEncoded
    @POST("api/descarga.htm")
    Call<List<InComercios>> download(@Field("empresa") Integer empresa);


    @FormUrlEncoded
    @POST("api/carga.htm")
    Call<InComercios> upload(@Field("empresa") Integer empresa);
}
