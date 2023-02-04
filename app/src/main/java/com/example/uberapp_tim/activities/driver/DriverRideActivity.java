package com.example.uberapp_tim.activities.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.InRideChatActivity;
import com.example.uberapp_tim.fragments.RideFragment;
import com.example.uberapp_tim.model.route.Location;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.model.Duration;

public class DriverRideActivity extends AppCompatActivity implements FragmentToActivity {

    Long rideId;
    Long passengerId;

    MaterialButton startRideBtn;
    MaterialButton endRideBtn;
    FloatingActionButton messageBtn;
    ExtendedFloatingActionButton panicBtn;
    TextView countdownTV;
    TextView kmTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rideId = getIntent().getLongExtra("id", 0);

        setContentView(R.layout.activity_ride);

        startRideBtn = findViewById(R.id.start_ride_btn);
        endRideBtn = findViewById(R.id.end_ride_btn);
        messageBtn = findViewById(R.id.message_btn_ride);
        panicBtn = findViewById(R.id.panic_btn);
        countdownTV = findViewById(R.id.countdown_time);
        kmTV = findViewById(R.id.countdown_distance);

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(DriverRideActivity.this, InRideChatActivity.class);
                chatIntent.putExtra("rideId", rideId);
                chatIntent.putExtra("userId", passengerId);
                startActivity(chatIntent);
            }
        });
    }

    public Bundle getIdBundle(){
        Bundle bundle = new Bundle();
        bundle.putLong("id", rideId);
        return bundle;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FragmentTransition.to(RideFragment.newInstance(), this, true);
        }
    }


    @Override
    public void communicate(String msg) {

    }

    @Override
    public void sendStartLocation(Location location) {

    }

    @Override
    public void sendFinishLocation(Location location) {

    }

    @Override
    public void sendRideData(float distance, Duration duration) {

    }

    @Override
    public void saveLatLng(String where, LatLng latLng) {

    }

    @Override
    public void communicate(Long value) {
        passengerId = value;
    }
}