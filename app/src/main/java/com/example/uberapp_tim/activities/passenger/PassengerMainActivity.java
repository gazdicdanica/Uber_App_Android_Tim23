package com.example.uberapp_tim.activities.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
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
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("AirRide");
        }

        passengerNav = findViewById(R.id.passengerNav);
        passengerNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        return true;
                    case (R.id.action_account):
                        i = new Intent(PassengerMainActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_inbox):
                        i = new Intent(PassengerMainActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_reports):
                        i = new Intent(PassengerMainActivity.this, PassengerReportsActivity.class);
                        startActivity(i);
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
        }

        return super.onOptionsItemSelected(item);
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
