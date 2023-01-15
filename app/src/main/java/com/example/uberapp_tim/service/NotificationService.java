package com.example.uberapp_tim.service;

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

import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.model.users.Driver;
import com.example.uberapp_tim.tools.AppTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotificationService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        int status = AppTools.getConnectivityStatus(getApplicationContext());

        if(status == AppTools.TYPE_WIFI || status == AppTools.TYPE_MOBILE){
            executor.execute( () ->{
                Log.i("REZ", "Background work here");
                handler.post(() ->{
                    Intent ints = new Intent(DriverMainActivity.NEW_RIDE);
                    getApplicationContext().sendBroadcast(ints);
                });
            });
        }

        stopSelf();

        return START_NOT_STICKY;
    }

}