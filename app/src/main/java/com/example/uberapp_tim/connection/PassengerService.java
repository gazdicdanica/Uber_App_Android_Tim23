package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.model.users.Passenger;
import com.example.uberapp_tim.model.users.User;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PassengerService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("passenger")
    Call<User> createPassenger(@Body Passenger passenger);

    @GET("passenger/{id}")
    Call<User> getPassenger(@Header("authorization") String token, @Path("id") Long id);

    @PUT("passenger/{id}")
    Call<User> updatePassenger(@Header("authorization") String token, @Path("id") Long id, @Body User user);
}
