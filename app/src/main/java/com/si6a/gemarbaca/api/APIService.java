package com.si6a.gemarbaca.api;

import com.si6a.gemarbaca.model.ResponseData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @POST("/api/register")
    Call<ResponseData> register(@Body RequestBody requestBody);

    @POST("/api/login")
    Call<ResponseData> login(@Body RequestBody requestBody);

    /**
     * Gemar Baca API
     */

    @GET("/api/gemar-baca")
    Call<ResponseData> fetchAllGemarBaca();

    @POST("/api/gemar-baca")
    Call<ResponseData> storeGemarBaca(@Body RequestBody requestBody);

    @PUT("/api/gemar-baca/{id}")
    Call<ResponseData> updateGemarBaca(@Path("id") String id, @Body RequestBody requestBody);

    @DELETE("/api/gemar-baca/{id}")
    Call<ResponseData> deleteGemarBaca(@Path("id") String id);
}
