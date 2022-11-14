package com.example.uberapp_tim.activities.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PassengerMainActivity extends AppCompatActivity {

    BottomNavigationView passengerNav;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_main_activity);

        Toolbar tb = findViewById(R.id.toolbarPassengerMain);
        setSupportActionBar(tb);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        passengerNav = findViewById(R.id.passengerNav);
        passengerNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(PassengerMainActivity.this, PassengerMainActivity.class);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(PassengerMainActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_inbox):
                        i = new Intent(PassengerMainActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_history):
                        i = new Intent(PassengerMainActivity.this, RideHistoryActivity.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });

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
        passengerNav.setSelectedItemId(R.id.action_main);
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
