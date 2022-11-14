package com.example.uberapp_tim.activities.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverAccountActivity;
import com.example.uberapp_tim.activities.driver.DriverInboxActivity;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.activities.driver.DriverRideHistoryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import model.users.Passenger;

public class PassengerRideHistoryActivity extends AppCompatActivity {

    BottomNavigationView historyNav;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ride_history_activity);

        Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        historyNav = findViewById(R.id.historyNav);
        historyNav.setSelectedItemId(R.id.action_history);
        historyNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(PassengerRideHistoryActivity.this, PassengerMainActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(PassengerRideHistoryActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_inbox):

                        i = new Intent(PassengerRideHistoryActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_history):
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
                overridePendingTransition(0,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        historyNav.setSelectedItemId(R.id.action_history);
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
