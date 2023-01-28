package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.dto.ReviewDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {

    @POST("review/{rideId}/driver")
    Call<ResponseBody> createReviewDriver(@Header("authorization") String token, @Path("rideId")Long rideId, @Body ReviewDTO reviewDTO);

    @POST("review/{rideId}/vehicle")
    Call<ResponseBody> createReviewVehicle(@Header("authorization") String token,@Path("rideId") Long rideId, @Body ReviewDTO reviewDTO);
}
