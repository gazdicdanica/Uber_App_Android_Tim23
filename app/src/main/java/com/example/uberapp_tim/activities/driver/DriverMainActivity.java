package com.example.uberapp_tim.activities.driver;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.fragments.MapFragment;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.ride.Ride;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainActivity extends AppCompatActivity {

    public static final String NEW_RIDE = "NEW_RIDE";

    private Long id;

    public static WebSocket webSocket;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        webSocket = new WebSocket();
        webSocket.stompClient.topic("/ride").subscribe(topicMessage -> {

            Log.wtf("LongOperation", topicMessage.getPayload());
            String rideMessage = topicMessage.getPayload();
            Gson g = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                    }
                }).create();
            }
            RideDTO ride = g.fromJson(rideMessage, RideDTO.class);
            if(ride.getDriver().getId() == DriverMainActivity.this.id){
                // send notification
            }
        });

        String id = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null);
        this.id = Long.valueOf(id);

        setContentView(R.layout.driver_main);

        Toolbar toolbar = findViewById(R.id.toolbarDriverMain);
        setSupportActionBar(toolbar);

        FragmentTransition.to(MapFragment.newInstance(), this, false);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        BottomNavigationView driverNav = findViewById(R.id.driverNav);
        driverNav.setSelectedItemId(R.id.action_main);
        driverNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        DriverMainActivity.this.recreate();
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(DriverMainActivity.this, DriverAccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_inbox):
                        i = new Intent(DriverMainActivity.this, DriverInboxActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_history):
                        i = new Intent(DriverMainActivity.this, RideHistoryActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        ToggleButton toggle = (ToggleButton) findViewById(R.id.status_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ServiceUtils.driverService.startShift(DriverMainActivity.this.id).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200){
                                Toast.makeText(DriverMainActivity.this, "Online", Toast.LENGTH_SHORT).show();
                            }else if (response.code() == 400){
                                // TODO add notification about exceeding time online
                                toggle.setChecked(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } else {

                    ServiceUtils.driverService.endShift(DriverMainActivity.this.id).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200){
                                Toast.makeText(DriverMainActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        ServiceUtils.driverService.startShift(DriverMainActivity.this.id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
                overridePendingTransition(0,0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FragmentTransition.to(MapFragment.newInstance(), this, false);
        BottomNavigationView nav = findViewById(R.id.driverNav);
        nav.getMenu().getItem(0).setChecked(true);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
