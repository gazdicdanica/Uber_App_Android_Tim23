package com.example.uberapp_tim.activities.passenger;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.activities.driver.DriverRideActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.RideRequestDTO;
import com.example.uberapp_tim.dto.UserShortDTO;
import com.example.uberapp_tim.fragments.DrawRouteFragment;
import com.example.uberapp_tim.fragments.MapFragment;
import com.example.uberapp_tim.fragments.RideFragment;
import com.example.uberapp_tim.model.ride.Ride;
import com.example.uberapp_tim.model.route.Location;
import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.model.vehicle.CarType;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.maps.model.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.O)
public class PassengerMainActivity extends AppCompatActivity implements View.OnClickListener, FragmentToActivity, AdapterView.OnItemSelectedListener {

    private static final DecimalFormat dfSharp = new DecimalFormat("#.##");
    private Route route = new Route();
    BottomNavigationView passengerNav;
    Button btnTimePicker;
    TextInputLayout ly1, ly2, l3;
    TextInputEditText txtTime, distanceTxt, estimateTxt, priceTxt;
    private int hour, minute;
    LocalDateTime startTime;
    LocalDateTime endTime;
    TextInputEditText start;
    TextInputEditText finish;
    ToggleButton pets;
    ToggleButton babies;
    Spinner carType;
    String carTypeSelected = "STANDARD";
    CircleButton requestRide;
    private int hour1=0, minute1=0;
    private com.example.uberapp_tim.model.route.Location startLocation = null;
    private com.example.uberapp_tim.model.route.Location endLocation = null;
    private float distance=0;
    private int delta=0;
    private double duration;
    private boolean isClicked = false;
    String startLoc="", finishLoc="";
    LatLng s, f;
    String timeData="";

    private RideRequestDTO rideDTO = new RideRequestDTO();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_main_activity);

        btnTimePicker = findViewById(R.id.btnTimePicker);
        btnTimePicker.setOnClickListener(this);
        requestRide = findViewById(R.id.requestRide);
        requestRide.setOnClickListener(this);

        ly1 = findViewById(R.id.distLayout);
        ly2 = findViewById(R.id.timeLayout);
        l3 = findViewById(R.id.priceLay);
        priceTxt = findViewById(R.id.price);
        txtTime = findViewById(R.id.time);
        distanceTxt = findViewById(R.id.distance);
        estimateTxt = findViewById(R.id.timeTctLayout);
        start = findViewById(R.id.startLocation);
        finish = findViewById(R.id.whereTo);

        carType = findViewById(R.id.carType);
        carType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CarType.values()));

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }

        babies = findViewById(R.id.babiesInRide);
        pets = findViewById(R.id.petsInRide);


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
    public void communicate(String s) {
        String id = s.split(",")[0];
        String val = s.split(",")[1];
        if (id.equals("start")) {
            startLoc = val;
            start.setText(val.split(",")[0]);
        } else if (id.equals("finish")) {
            finishLoc = val;
            finish.setText(val.split(",")[0]);
        }
    }

    @Override
    public void sendStartLocation(com.example.uberapp_tim.model.route.Location location) {
        startLocation = location;
    }

    @Override
    public void sendFinishLocation(Location location) {
        endLocation = location;
    }

    @Override
    public void sendRideData(float distance, Duration duration) {
        timeData = duration.humanReadable;
        this.duration = (double) duration.inSeconds/60;
        this.distance = distance;

        String txt = timeData+"";
        estimateTxt.setText(txt);

        txt = dfSharp.format(this.distance)+"km";
        distanceTxt.setText(txt);

        txt = dfSharp.format(estimatePrice())+"RSD";
        priceTxt.setText(txt);
    }

    @Override
    public void saveLatLng(String where, LatLng latLng) {
        if (where.equals("s")) {
            s = latLng;
        } else {
            f = latLng;
        }
    }

    @Override
    public void communicate(Long value) {

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
        FragmentTransition.to(MapFragment.newInstance(), this, false);
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
                    String txt = hourOfDay + ":" + minuteOfDay;
                    txtTime.setText(txt);
                }}, hour, minute, true);
            timePickerDialog.show();
        } else if (view == requestRide) {
            if (start != null && finish != null) {
                if (isClicked) {
                    requestRide.setEnabled(false);
                    createRide();
                    createRoute();
                    this.rideDTO.addRoute(route);
                    ServiceUtils.rideService.createRide(this.rideDTO).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(PassengerMainActivity.this, "Waiting For Driver", Toast.LENGTH_SHORT).show();
                            Gson g = null;
                            try {
                                String json = response.body().string();
                                g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

                                    @Override
                                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                                    }
                                }).create();
                                Ride ride = g.fromJson(json, Ride.class);
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                Toast.makeText(PassengerMainActivity.this, "RIDE_ID "+ride.getId(), Toast.LENGTH_SHORT).show();
                                requestRide.setColor(Color.RED);
                                requestRide.setImageResource(0);
                                requestRide.setBackgroundResource(R.drawable.ic_baseline_error_outline_24);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(PassengerMainActivity.this, "Greska 569", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                isClicked = true;
                btnTimePicker.setVisibility(View.GONE);
                babies.setVisibility(View.GONE);
                pets.setVisibility(View.GONE);
                carType.setVisibility(View.GONE);
                txtTime.setVisibility(View.GONE);

                ly1.setVisibility(View.VISIBLE);
                ly2.setVisibility(View.VISIBLE);
                l3.setVisibility(View.VISIBLE);
                estimateTxt.setVisibility(View.VISIBLE);
                estimateTxt.setEnabled(false);
                distanceTxt.setVisibility(View.VISIBLE);
                distanceTxt.setEnabled(false);
                priceTxt.setVisibility(View.VISIBLE);
                priceTxt.setEnabled(false);

                FragmentTransition.to(DrawRouteFragment.newInstance(), this, false);
            }
        }
    }

    public Bundle rideBundle(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("start", this.s);
        bundle.putParcelable("finish", this.f);
        return bundle;

    }

    private void createRide() {
        this.rideDTO.setBabyTransport(babies.isChecked());
        this.rideDTO.setPetTransport(pets.isChecked());
        this.rideDTO.setVehicleType(resolveCarType());
        this.rideDTO.setDelayInMinutes(calcTimeForRideAppoint());
        this.rideDTO.addUser(new UserShortDTO(
                Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)),
                getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("email", null)));
    }

    private void createRoute() {
        startLocation.setAddress(startLoc);
        endLocation.setAddress(finishLoc);
        route.setDeparture(startLocation);
        route.setDestination(endLocation);
        rideDTO.setEstimatedPrice(estimatePrice());
        rideDTO.setEstimatedTime(duration);
        route.setDistance(distance);
        if ((hour1 != 0) && (minute1 != 0)){
            rideDTO.setStartTime(LocalDateTime.now());
        } else {
            LocalDateTime time = LocalDateTime.now();
            time.plusMinutes(calcTimeForRideAppoint());
            rideDTO.setStartTime(time);
        }
        rideDTO.setEndTime(rideDTO.getStartTime().plusMinutes((long) duration));

    }

    private float estimatePrice() {
        if (carTypeSelected.equals("STANDARD")) {
            return 300 + 120*distance;
        } else if (carTypeSelected.equals("LUXURY")) {
            return 500 + 120*distance;
        } else {
            return 400 + 12*distance;
        }
    }


    public int calcTimeForRideAppoint(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime timeNow = LocalTime.now();
            int deltaH, deltaM;
            if (hour1 != 0 && minute1 != 0) {
                deltaH = Math.abs(hour1 - timeNow.getHour());
                deltaM = Math.abs(minute1 - timeNow.getMinute());
                return deltaH * 60 + deltaM;
            }
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
        LatLng p1;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            if (addresses == null) {
                return null;
            }
            Address location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude() * 1E6), (location.getLongitude() * 1E6));
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
                Log.println(Log.ASSERT, "ADRESAAA", ""+address);
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
