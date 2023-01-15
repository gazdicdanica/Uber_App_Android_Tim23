package com.example.uberapp_tim.activities.driver;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.fragments.MapFragment;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.ride.Rejection;
import com.example.uberapp_tim.receiver.NotificationReceiver;
import com.example.uberapp_tim.service.NotificationService;
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
    public static NotificationReceiver notificationReceiver;
    public static NotificationService notificationService;
    private PendingIntent pendingIntent;
    public static RideDTO ride;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        createNotificationChannel("DriverNotification", "New ride notifications", "driver");

        webSocket = new WebSocket();
        webSocket.stompClient.topic("/ride").subscribe(topicMessage -> {

            String rideMessage = topicMessage.getPayload();
            Gson g = null;

            Log.wtf("message", rideMessage);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                    }
                }).create();
            }
            ride = g.fromJson(rideMessage, RideDTO.class);

//            if(Objects.equals(ride.getDriver().getId(), id)){
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showNewRideDialog();
                    }
                });
//            }
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
                Toast.makeText(DriverMainActivity.this, "Online", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void showNewRideDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
        LayoutInflater inflater = DriverMainActivity.this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_driver_new_ride, null);

        final EditText startLocation = dialogView.findViewById(R.id.start_location_dialog);
        final EditText endLocation = dialogView.findViewById(R.id.end_location_dialog);
        final EditText time = dialogView.findViewById(R.id.dialog_time);
        final EditText km = dialogView.findViewById(R.id.dialog_kilometers);
        final EditText people = dialogView.findViewById(R.id.person_num_dialog);
        final EditText price = dialogView.findViewById(R.id.price_dialog);


        startLocation.setText(ride.getLocations().get(0).getDeparture().getAddress());
        endLocation.setText(ride.getLocations().get(0).getDestination().getAddress());
        String value = String.valueOf(ride.getEstimatedTimeInMinutes()) + " min";
        time.setText(value);
        value = String.valueOf(ride.getLocations().get(0).getDistance()) + " km";
        km.setText(value);
        people.setText(String.valueOf(ride.getPassengers().size()));
        value = String.valueOf(ride.getTotalCost()) + " RSD";
        price.setText(value);


        builder.setView(dialogView);

        builder.setNegativeButton(R.string.dialog_decline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDeclineDialog();
            }
        });
        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDeclineDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
        builder.setTitle("Please enter a reason");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(30,30,30,30);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reason = input.getText().toString();
                Rejection rejection = new Rejection();
                rejection.setReason(reason);

                ServiceUtils.rideService.cancelRide(ride.getId(), rejection).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(DriverMainActivity.this, "Ride canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        builder.create().show();
    }

    public void setUpReceiver(){
        notificationReceiver = new NotificationReceiver();

        Intent intent = new Intent(this, NotificationService.class);
        //add extra ride
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createNotificationChannel(String channelName, String channelDescription, String channelId){
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
