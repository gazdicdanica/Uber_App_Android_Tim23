package com.example.uberapp_tim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.uberapp_tim.activities.PassengerMainActivity;
import com.example.uberapp_tim.activities.SplashActivity;
import com.example.uberapp_tim.activities.UserLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}