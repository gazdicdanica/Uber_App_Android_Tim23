package com.example.uberapp_tim.connection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DriverService {

    @PUT("driver/{id}/working-hour/start")
    Call<ResponseBody> startShift(@Header("authorization") String token, @Path("id")Long id);

    @PUT("driver/{id}/working-hour/end")
    Call<ResponseBody> endShift(@Header("authorization") String token, @Path("id") Long id);
}
