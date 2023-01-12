package com.example.uberapp_tim.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.tools.AppTools;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.splash);
        int connection = AppTools.getConnectivityStatus(getApplicationContext());
        if(connection == AppTools.TYPE_WIFI || connection == AppTools.TYPE_MOBILE){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    finish();
                }
            }, 5000);
        }else {

        }

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
