package com.example.uberapp_tim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;

public class DriverPapersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.driver_papers_activity);

        Toolbar toolbar = findViewById(R.id.toolbarDriverPapers);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        setListeners();
    }

    public void setListeners(){
        Button confirm = findViewById(R.id.confirmDriverIds);
        EditText driverId = findViewById(R.id.idNum);
        EditText vehicleRegistration = findViewById(R.id.vehicleRegistrationIdNum);

        driverId.setText("1234567890", TextView.BufferType.EDITABLE);
        vehicleRegistration.setText("0987654321", TextView.BufferType.EDITABLE);

        driverId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverPapersActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Driver Licence Number: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        vehicleRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverPapersActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Vehicle Registration: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
