package com.clj.blesample.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @FormUrlEncoded
    @Headers("key:" + Config.KEY)
    @POST("api/login/cek")
    Call<ResponseGlobal> doLogin(
            @Field("email") String email,
            @Field("password") String pass);

    @FormUrlEncoded
    @Headers("key:" + Config.KEY)
    @POST("api/monitoring/insert")
    Call<ResponseMonitoring> doMonitoring(
            @Field("id") String id,
            @Field("hr") String hr,
            @Field("spo2") String spo2,
            @Field("bpm") String bpm,
            @Field("temp") String temp,
            @Field("systolic") String systolic,
            @Field("diastolic") String diastolic,
            @Field("tingkatKesadaran") String tingkatKesadaran,
            @Field("oksigenTambahan") String oksigenTambahan
    );
}
