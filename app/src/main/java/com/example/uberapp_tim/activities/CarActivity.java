package com.example.uberapp_tim.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;

public class CarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_car);

        Toolbar toolbar = findViewById(R.id.toolbarCar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        setListener();

    }

    public void setListener(){
        NumberPicker picker1 = findViewById(R.id.numberPicker);
        Button cont = findViewById(R.id.confirmCarParamButton);
        EditText carModel = findViewById(R.id.carModel);
        EditText registration = findViewById(R.id.registration);

        carModel.setText("Skoda Fabia", TextView.BufferType.EDITABLE);
        registration.setText("NS-133-XR");


        picker1.setMaxValue(8);
        picker1.setMinValue(1);
        picker1.setValue(4);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        carModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CarActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Car Model: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CarActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Registration: ");
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
