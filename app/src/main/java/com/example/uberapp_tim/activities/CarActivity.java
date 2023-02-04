package com.example.uberapp_tim.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.vehicle.Vehicle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarActivity extends AppCompatActivity {
    String jwt;
    Long id;
    Vehicle vehicle;
    NumberPicker picker1;
    Button cont;
    EditText carModel, registration;
    SwitchCompat babies, pets;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_car);


        picker1 = findViewById(R.id.numberPicker);
        picker1.setMaxValue(8);
        picker1.setMinValue(1);
        babies = findViewById(R.id.babySwitch);
        pets = findViewById(R.id.isPet);
        cont = findViewById(R.id.confirmCarParamButton);
        carModel = findViewById(R.id.carModel);
        registration = findViewById(R.id.registration);

        fillTextWithData();

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

    private void fillTextWithData() {
        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        jwt = pref.getString("accessToken", "");
        id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.driverService.getVehicle("Bearer "+jwt, id).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                vehicle = response.body();
                Log.i("VOZILO:", response.body().toString());
                setParameters(vehicle);
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Log.wtf("Get V:", t.getMessage());
            }
        });
    }

    private void setParameters(Vehicle vehicle) {
        carModel.setText(vehicle.getModel());
        registration.setText(vehicle.getLicenseNumber());
        babies.setChecked(vehicle.isBabyTransport());
        pets.setChecked(vehicle.isPetTransport());
        picker1.setValue(vehicle.getPassengerSeats());
    }

    public void setListener(){

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vehicle v = createVehicle();
                if (checkVehicleParamsChanged(v)) {
                    ServiceUtils.driverService.updateVehicle("Bearer "+jwt, id, v).enqueue(new Callback<Vehicle>() {
                        @Override
                        public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                            if (response.code() == 200) {
                                Toast.makeText(CarActivity.this, "Vehicle Params Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Vehicle> call, Throwable t) {
                            Log.wtf("Create V:", t.getMessage());
                        }
                    });
                } else {
                    finish();
                }
            }
        });

    }

    private Vehicle createVehicle() {
        Vehicle v = new Vehicle();
        v.setModel(carModel.getText().toString());
        v.setLicenseNumber(registration.getText().toString());
        v.setPassengerSeats(picker1.getValue());
        v.setPetTransport(pets.isChecked());
        v.setBabyTransport(babies.isChecked());
        v.setVehicleType(vehicle.getVehicleType());
        return v;
    }

    private boolean checkVehicleParamsChanged(Vehicle v) {
        if (v.getModel().equals(vehicle.getModel())) {
            if (v.getLicenseNumber().equals(vehicle.getLicenseNumber())) {
                if (v.getPassengerSeats() == vehicle.getPassengerSeats()) {
                    if (v.isBabyTransport() == vehicle.isBabyTransport()) {
                        if (v.isPetTransport() == vehicle.isPetTransport()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
