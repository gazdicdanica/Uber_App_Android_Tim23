package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.dto.RideRequestDTO;
import com.example.uberapp_tim.model.message.Panic;
import com.example.uberapp_tim.model.ride.Rejection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideService {

    @PUT("ride/{id}/cancel")
    Call<ResponseBody> cancelRide(@Header("authorization") String token, @Path("id")Long id, @Body Rejection rejection);

    @GET("ride/{id}")
    Call<ResponseBody> getRide(@Header("authorization") String token, @Path("id")Long id);

    @PUT("ride/{id}/accept")
    Call<ResponseBody> acceptRide(@Header("authorization") String token, @Path("id") Long id);

    @PUT("ride/{id}/start")
    Call<ResponseBody> startRide(@Header("authorization") String token, @Path("id")Long id);

    @PUT("ride/{id}/end")
    Call<ResponseBody> endRide(@Header("authorization") String token, @Path("id")Long id);

    @PUT("ride/{id}/panic")
    Call<ResponseBody> panicRide(@Header("authorization") String token, @Path("id")Long id, @Body Panic panic);

    @POST("ride")
    Call<ResponseBody> createRide(@Header("authorization") String token, @Body RideRequestDTO request);
}
