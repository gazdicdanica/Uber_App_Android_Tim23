package com.example.uberapp_tim.service;

import com.example.uberapp_tim.dto.LoginDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("login")
    Call<ResponseBody> login(@Body LoginDTO loginDTO);
}
