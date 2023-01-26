package com.example.uberapp_tim.connection;

import com.example.uberapp_tim.dto.LoginDTO;
import com.example.uberapp_tim.dto.SendMessageDTO;
import com.example.uberapp_tim.dto.TokensDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Call<TokensDTO> login(@Body LoginDTO loginDTO);

    @GET("user/{passengerId}/{driverId}/{rideId}/message")
    Call<ResponseBody> getMessagesForUsersByRide(@Path("passengerId")Long id1, @Path("driverId") Long id2, @Path("rideId")Long id3);

    @POST("user/{id}/message")
    Call<ResponseBody> sendMessage(@Path("id")Long id, @Body SendMessageDTO dto);
}
