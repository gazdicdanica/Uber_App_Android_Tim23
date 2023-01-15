package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.model.users.Passenger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PassengerService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("passenger")
    Call<ResponseBody> createPassenger(@Body Passenger passenger);
}
