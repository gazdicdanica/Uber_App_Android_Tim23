package com.example.uberapp_tim.activities.passenger;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.RideHistoryActivity;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.dto.RideRequestDTO;
import com.example.uberapp_tim.dto.UserShortDTO;
import com.example.uberapp_tim.model.vehicle.CarType;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class PassengerMainActivity extends AppCompatActivity implements View.OnClickListener, FragmentToActivity, AdapterView.OnItemSelectedListener {

    BottomNavigationView passengerNav;
    Button btnTimePicker;
    TextInputEditText txtTime;
    private int hour, minute;
    TextInputEditText start;
    TextInputEditText finish;
    ToggleButton pets;
    ToggleButton babies;
    Spinner carType;
    String carTypeSelected = "STANDARD";
    CircleButton requestRide;
    private int hour1, minute1;


    private RideRequestDTO rideDTO = new RideRequestDTO();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_main_activity);

        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnTimePicker.setOnClickListener(this);
        requestRide = findViewById(R.id.requestRide);
        requestRide.setOnClickListener(this);

        txtTime = (TextInputEditText) findViewById(R.id.timePickerId);
        start = (TextInputEditText) findViewById(R.id.startLocation);
        finish = (TextInputEditText) findViewById(R.id.whereTo);

        carType = findViewById(R.id.carType);
        carType.setAdapter(new ArrayAdapter<CarType>(this, android.R.layout.simple_spinner_item, CarType.values()));


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("AirRide");
        }

        babies = (ToggleButton) findViewById(R.id.babiesInRide);
        pets = (ToggleButton) findViewById(R.id.petsInRide);


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
    public void communicate(String s) {
        String id = s.split(",")[0];
        String val = s.split(",")[1];
        if (id.equals("start")) {
            start.setText(val);
        } else if (id.equals("finish")) {
            finish.setText(val);
        }
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

    @Override
    public void onClick(View view) {
        if(view == btnTimePicker) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                    hour1 = hourOfDay;
                    minute1 = minuteOfDay;
                    txtTime.setText(hourOfDay + ":" + minuteOfDay);
                }}, hour, minute, true);
            timePickerDialog.show();
        } else if (view == requestRide) {
            this.rideDTO.setBabyTransport(babies.isChecked());
            this.rideDTO.setPetTransport(pets.isChecked());
            this.rideDTO.setVehicleType(resolveCarType());
            this.rideDTO.setDelayInMinutes(calcTimeForRideAppoint());
            rideDTO.addUser(new UserShortDTO(
                    Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)),
                    getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("email", null)
            ));
        }
    }

    public int calcTimeForRideAppoint(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime timeNow = LocalTime.now();
            int deltaH, deltaM;
            deltaH = Math.abs(hour1 - timeNow.getHour());
            deltaM = Math.abs(minute1 - timeNow.getMinute());
            return (int) deltaH*60 + deltaM;
        }
        return 0;
    }

    public CarType resolveCarType() {
        CarType type = CarType.STANDARD;
        if (carType.getSelectedItem().toString().equals("LUXURY")) {
            type = CarType.LUXURY;
        } else if (carType.getSelectedItem().toString().equals("VAN")) {
            type = CarType.VAN;
        }
        return type;
    }
    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        LatLng p1 = null;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            if (addresses == null) {
                return null;
            }
            Address location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((double) (location.getLatitude() * 1E6), (double) (location.getLongitude() * 1E6));
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAddressFromLocation(LatLng location) {
        String address="";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if(addresses != null){
                Address returned = addresses.get(0);

                address = returned.getAddressLine(0);
            } else {
                address = "No Address Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        carTypeSelected = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
