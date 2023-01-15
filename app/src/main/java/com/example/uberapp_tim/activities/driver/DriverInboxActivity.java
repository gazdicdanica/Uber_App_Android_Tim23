package com.example.uberapp_tim.activities.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.example.uberapp_tim.fragments.InboxFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DriverInboxActivity extends AppCompatActivity {

    BottomNavigationView driverNav;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.driver_inbox_activity);

        Toolbar toolbar = findViewById(R.id.toolbarDriverInbox);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        driverNav = findViewById(R.id.driverInboxNav);
        driverNav.setSelectedItemId(R.id.action_inbox);
        driverNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(DriverInboxActivity.this, DriverMainActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(DriverInboxActivity.this, DriverAccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_inbox):
                        return true;

                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true).add(R.id.driverInboxContainer, InboxFragment.class, null)
                .commit();
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
        driverNav.setSelectedItemId(R.id.action_inbox);
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
