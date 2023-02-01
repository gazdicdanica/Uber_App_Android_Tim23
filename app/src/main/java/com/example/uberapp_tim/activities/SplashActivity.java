package com.example.uberapp_tim.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.activities.passenger.PassengerMainActivity;
import com.example.uberapp_tim.dialogs.LocationDialog;
import com.example.uberapp_tim.tools.AppTools;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    public static final int REQUEST_CODE = 123;

    private boolean checkPermissions(){

        int networkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return networkPermission == PackageManager.PERMISSION_GRANTED && locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE) {
            if(checkPermissions()){
                continueOperation();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("Allow user location")
                            .setMessage("The app needs the necessary permissions to function properly. Allow now?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(SplashActivity.this,
                                            new String[]{
                                                    Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE
                                    );
                                }
                            })
                            .create()
                            .show();

                }else{
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("Permission denied")
                            .setMessage("The app needs the necessary permissions to function properly. Please go to the app settings and grant the permissions.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).create().show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.splash);
        int connection = AppTools.getConnectivityStatus(getApplicationContext());
        if(checkPermissions()){
            if(connection == AppTools.TYPE_WIFI || connection == AppTools.TYPE_MOBILE){
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        continueOperation();
                    }
                }, 3000);
            }
            else{
                new AlertDialog.Builder(SplashActivity.this)
                        .setTitle("No network provider")
                        .setMessage("The app needs network to function properly. Please connect and try again")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();
            }
        }else{
            requestPermissions();
        }


    }

    private void continueOperation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{

                    String accessToken = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                    if(accessToken.length() == 0){
                        startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                        finish();
                    }else{
                        switch(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("role", "")){
                            case "ROLE_USER":
                                startActivity(new Intent(SplashActivity.this, PassengerMainActivity.class));
                                finish();
                                break;
                            case "ROLE_DRIVER":
                                startActivity(new Intent(SplashActivity.this, DriverMainActivity.class));
                                finish();
                        }

                    }
                }catch(NullPointerException ex){
                    ex.printStackTrace();
                }
            }
        }, 5000);
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
    protected void onResume() {
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
