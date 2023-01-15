package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.fragments.RideFragment;
import com.example.uberapp_tim.tools.FragmentTransition;

public class RideActivity extends AppCompatActivity {

    Long rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rideId = getIntent().getLongExtra("id", 0);
        if(rideId == 0){
            return;
        }

        setContentView(R.layout.activity_ride);

        FragmentTransition.to(RideFragment.newInstance(), this, false);
    }
}