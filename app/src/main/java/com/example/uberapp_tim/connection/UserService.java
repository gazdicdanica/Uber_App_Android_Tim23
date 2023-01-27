package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.dto.LoginDTO;
import com.example.uberapp_tim.dto.SendMessageDTO;
import com.example.uberapp_tim.dto.TokensDTO;
import com.example.uberapp_tim.dto.UpdatePasswordDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Call<TokensDTO> login(@Body LoginDTO loginDTO);

    @GET("user/{passengerId}/{driverId}/{rideId}/message")
    Call<ResponseBody> getMessagesForUsersByRide(@Header("authorization") String token, @Path("passengerId")Long id1, @Path("driverId") Long id2, @Path("rideId")Long id3);

    @POST("user/{id}/message")
    Call<ResponseBody> sendMessage(@Header("authorization") String token, @Path("id")Long id, @Body SendMessageDTO dto);

    @PUT("user/{id}/changePassword")
    Call<ResponseBody> changePassword(@Header("authorization") String token, @Path("id")Long id, @Body UpdatePasswordDTO dto);
}
