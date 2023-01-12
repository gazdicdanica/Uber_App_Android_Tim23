package com.example.uberapp_tim.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DriverService {

    @POST("driver/{id}/working-hour")
    Call<ResponseBody> startShift(@Path("id")Long id);

}
