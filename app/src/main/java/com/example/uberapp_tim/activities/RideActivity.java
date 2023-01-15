package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.fragments.RideFragment;
import com.example.uberapp_tim.model.route.Location;
import com.example.uberapp_tim.service.ActivityToFragment;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideActivity extends AppCompatActivity implements FragmentToActivity {

    Long rideId;
    RideDTO ride;

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


        startRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRideBtn.setVisibility(View.GONE);
                endRideBtn.setVisibility(View.VISIBLE);
                panicBtn.setVisibility(View.VISIBLE);

            }
        });

        Fragment fragment = RideFragment.newInstance();
        FragmentTransition.to(fragment, RideActivity.this, false);
    }

    public Bundle getIdBundle(){
        Bundle bundle = new Bundle();
        bundle.putLong("id", rideId);
        return bundle;
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
}