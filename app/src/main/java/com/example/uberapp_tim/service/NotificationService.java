package com.example.uberapp_tim.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.tools.AppTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    public NotificationService() {
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//        int status = AppTools.getConnectivityStatus(getApplicationContext());
//
//        if(status == AppTools.TYPE_WIFI || status == AppTools.TYPE_MOBILE){
//            executor.execute( () -> {
//                handler.post( () -> {
//                    Intent intent1 = new Intent(String.valueOf(R.string.new_ride_notification));
//                    intent1.putExtra()
//                })
//            });
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}