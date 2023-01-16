package com.example.uberapp_tim.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent){
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, );
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "driver");

        Intent wiFiintent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, wiFiintent, 0);


        builder.setContentTitle("HI");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
