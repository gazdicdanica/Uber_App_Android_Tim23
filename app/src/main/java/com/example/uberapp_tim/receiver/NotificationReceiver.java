package com.example.uberapp_tim.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static String CHANNEL_ID = "Driver Ride Channel";

    @Override
    public void onReceive(Context context, Intent intent){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

//        if(intent.getAction().equals(R.string.new_ride_notification)){
//
//        }
    }
}
