package com.example.uberapp_tim.activities.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.adapters.ChatAdapter;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.MessageDTO;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerInboxActivity extends AppCompatActivity implements SensorEventListener {

    SharedPreferences sharedPreferences;
    Map<Long, List<MessageDTO>> data = new LinkedHashMap<>();

    SensorManager sensorManager;
    private float mAccel, mAccelCur, mAccelLast;

    BottomNavigationView passengerNav;

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_inbox_activity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbarPassengerInbox);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        recycler = (RecyclerView) findViewById(R.id.recycler_inbox);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        passengerNav = findViewById(R.id.passengerInboxNav);
        passengerNav.setSelectedItemId(R.id.action_inbox);
        passengerNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(PassengerInboxActivity.this, PassengerMainActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_account):
                        i = new Intent(PassengerInboxActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_inbox):
                        return true;

                    case (R.id.action_reports):
                        i = new Intent(PassengerInboxActivity.this, PassengerReportsActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        sharedPreferences = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);

        Long userId = Long.valueOf(sharedPreferences.getString("id", null));
        ServiceUtils.userService.getAllMessages("Bearer "+sharedPreferences.getString("accessToken", null), userId).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Gson g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                            @Override
                            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                            }
                        }).create();
                        Type listType = new TypeToken<ArrayList<MessageDTO>>(){}.getType();

                        String message = response.body().string();
                        List<MessageDTO> messages = new ArrayList<>();
                        messages = g.fromJson(message,listType);

                        if(messages!= null){
                            for(MessageDTO m : messages){
                                if(Objects.equals(m.getSender().getId(), userId)){
                                    if(data.containsKey(m.getReceiver().getId())){
                                        data.get(m.getReceiver().getId()).add(m);
                                    }else{
                                        Log.d("tu sam", "prvi if");
                                        List<MessageDTO> list = new ArrayList<>();
                                        list.add(m);
                                        data.put(m.getReceiver().getId(), list);
                                    }
                                }else{
                                    if(data.containsKey(m.getSender().getId())){
                                        data.get(m.getSender().getId()).add(m);
                                    }else{
                                        Log.d("tu sam", "drugi if");
                                        List<MessageDTO> list = new ArrayList<>();
                                        list.add(m);
                                        data.put(m.getSender().getId(), list);
                                    }

                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        ChatAdapter adapter = new ChatAdapter(PassengerInboxActivity.this, data);

                        Log.d("DYING", "FETUS");
                        recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                }
        );

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
        passengerNav.setSelectedItemId(R.id.action_inbox);
        Objects.requireNonNull(sensorManager).registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
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
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        mAccelLast = mAccelCur;
        mAccelCur = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCur - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        if (mAccel > 12) {
            Toast.makeText(this, "SHAKE EVENT", Toast.LENGTH_SHORT).show();
            Map<Long, List<MessageDTO>> sortedMap = data.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            ChatAdapter adapter = new ChatAdapter(PassengerInboxActivity.this, sortedMap);
            recycler.setAdapter(adapter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
