package com.example.uberapp_tim.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.service.NotificationService;

public class NotificationReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("RECEIVER", " HERE");
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("title", intent.getStringExtra("title"));
        serviceIntent.putExtra("text", intent.getStringExtra("text"));
        serviceIntent.putExtra("channel", intent.getStringExtra("channel"));
        serviceIntent.putExtra("bigText", intent.getStringExtra("bigText"));
        Log.wtf("BIG TEXT", intent.getStringExtra("bigText"));
        serviceIntent.putExtra("id", intent.getStringExtra("id"));
        context.startService(serviceIntent);

    }
}
