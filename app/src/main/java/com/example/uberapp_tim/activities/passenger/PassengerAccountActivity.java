package com.example.uberapp_tim.activities.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_account_activity);

        Toolbar toolbar = findViewById(R.id.toolbarPassAcc);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //TODO set parameters in edittext
        setListeners();

        BottomNavigationView passengerNav = findViewById(R.id.passNav);
        passengerNav.setSelectedItemId(R.id.action_account);
        passengerNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(PassengerAccountActivity.this, PassengerMainActivity.class);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(PassengerAccountActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_inbox):
                        i = new Intent(PassengerAccountActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_history):
                        i = new Intent(PassengerAccountActivity.this, RideHistoryActivity.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });

    }


    private void setListeners(){
        TextView fullName = findViewById(R.id.user_full_name);
        EditText email = findViewById(R.id.user_email);
        EditText address = findViewById(R.id.user_address);
        EditText phone = findViewById(R.id.user_phone);
        EditText password = findViewById(R.id.user_password);

        fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Name");
                bundle.putString("label2", "Last name");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "E-mail");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Address");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Phone");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Old password");
                bundle.putString("label2", "New password");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
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
