package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.fragments.RideFragment;
import com.example.uberapp_tim.model.route.Location;
import com.example.uberapp_tim.service.ActivityToFragment;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.example.uberapp_tim.tools.FragmentTransition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideActivity extends AppCompatActivity implements FragmentToActivity {

    Long rideId;
    RideDTO ride;
    ActivityToFragment aTof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rideId = getIntent().getLongExtra("id", 0);
        if(rideId == 0){
            return;
        }

        Log.d("RIDE ID EXTRAS", String.valueOf(rideId));

        setContentView(R.layout.activity_ride);

        ServiceUtils.rideService.getRide(rideId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("CODE", String.valueOf(response.code()));
                if(response.code() == 200){
                    try {
                        String rideMessage = response.body().string();

                        Gson g = null;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                                @Override
                                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                                }
                            }).create();
                        }
                        ride = g.fromJson(rideMessage, RideDTO.class);
                        Log.d(" id " , String.valueOf(ride.getId()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RideActivity.this, "Fail", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

        Fragment fragment = RideFragment.newInstance();
        FragmentTransition.to(fragment, RideActivity.this, false);
    }

    public Bundle getIdBundle(){
        Bundle bundle = new Bundle();
        bundle.putLong("id", rideId);
        return bundle;
    }

    @Override
    public void communicate(String msg) {

    }

    @Override
    public void sendStartLocation(Location location) {

    }

    @Override
    public void sendFinishLocation(Location location) {

    }
}