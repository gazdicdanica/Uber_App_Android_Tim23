package com.example.uberapp_tim.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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


        if(intent.getAction().equals(DriverMainActivity.NEW_RIDE)){
            String result = intent.getExtras().getString("result");
        }
    }
}
