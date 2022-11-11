package com.example.uberapp_tim.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.example.uberapp_tim.R;

public class DriverAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Toast.makeText(this, "DriverAccAct", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.driver_account_activity);


        Button car = findViewById(R.id.carButton);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverAccountActivity.this, CarActivity.class));
            }
        });

        Button documents = findViewById(R.id.papersButton);
        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverAccountActivity.this, DriverPapersActivity.class));
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
