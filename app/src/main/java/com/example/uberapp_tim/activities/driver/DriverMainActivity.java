package com.example.uberapp_tim.activities.driver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.example.uberapp_tim.fragments.MapFragment;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class DriverMainActivity extends AppCompatActivity {

    Long id;

    WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        String id = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null);
        this.id = Long.valueOf(id);
        Log.d("id", this.id.toString());

        setContentView(R.layout.driver_main);

        Toolbar toolbar = findViewById(R.id.toolbarDriverMain);
        setSupportActionBar(toolbar);

        FragmentTransition.to(MapFragment.newInstance(), this, false);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setHomeButtonEnabled(true);
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

    private void createWebSocketClient(){
        URI uri = null;
        try {
            uri = new URI("http://192.168.0.21:8080/socket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
            }

            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket", "Message received");

            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onCloseReceived() {

            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private static String CHANNEL_ID = "Driver Ride Channel";

    private void createNotificationChannel(){
        CharSequence name = "Ride notification channel";
        String description = "Accept/decline new ride";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
