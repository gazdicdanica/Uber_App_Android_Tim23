package com.example.uberapp_tim.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.model.users.Driver;
import com.example.uberapp_tim.tools.AppTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotificationService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "DRIVER";

    @Override
    public void onCreate(){
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String notificationTitle = intent.getStringExtra("title");
        String notificationText = intent.getStringExtra("text");
        String channel = intent.getStringExtra("channel");
        Long id = Long.valueOf(intent.getStringExtra("id"));

        if(id.equals(Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)))){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }



        return START_NOT_STICKY;
    }

}