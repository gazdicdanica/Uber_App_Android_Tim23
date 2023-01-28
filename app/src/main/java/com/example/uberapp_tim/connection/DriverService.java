package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.model.users.User;
import com.example.uberapp_tim.model.vehicle.Document;
import com.example.uberapp_tim.model.vehicle.Vehicle;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DriverService {

    @PUT("driver/{id}/working-hour/start")
    Call<ResponseBody> startShift(@Header("authorization") String token, @Path("id")Long id);

    @PUT("driver/{id}/working-hour/end")
    Call<ResponseBody> endShift(@Header("authorization") String token, @Path("id") Long id);

    @GET("driver/{id}")
    Call<User> getDriver(@Header("authorization")String token, @Path("id")Long id);

    @PUT("driver/{id}")
    Call<User> updateDriver(@Header("authorization") String jwt,@Path("id")Long id,@Body User user);

    @GET("driver/{id}/vehicle")
    Call<Vehicle> getVehicle(@Header("authorization") String jwt,@Path("id") Long id);

    @PUT("driver/{id}/vehicle")
    Call<Vehicle> updateVehicle(@Header("authorization") String jwt, @Path("id")Long id, @Body Vehicle vehicle);

    @GET("driver/{id}/documents")
    Call<List<Document>> getDriverDoc(@Header("authorization")String jwt, @Path("id")Long id);

    @POST("driver/{id}/documents")
    Call<Document> addDriverDoc(@Header("authorization")String jwt, @Path("id")Long id, @Body Document doc);
}
