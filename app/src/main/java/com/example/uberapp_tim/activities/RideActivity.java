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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideActivity extends AppCompatActivity implements FragmentToActivity {

    RideDTO ride;
    ActivityToFragment aTof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long rideId = getIntent().getLongExtra("id", 0);
        if(rideId == 0){
            return;
        }

        Log.d("RIDE ID EXTRAS", String.valueOf(rideId));

        setContentView(R.layout.activity_ride);

        Bundle bundle = new Bundle();
        bundle.putLong("id", rideId);

        Fragment fragment = RideFragment.newInstance();
        fragment.setArguments(bundle);
        Log.d("Bundle", String.valueOf(bundle.getLong("id")));
        FragmentTransition.to(fragment, this);
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