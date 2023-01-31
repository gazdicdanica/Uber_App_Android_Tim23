package com.example.uberapp_tim.service;


import com.example.uberapp_tim.model.route.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.Duration;

public interface FragmentToActivity {
    void communicate(String msg);
    void sendStartLocation(Location location);
    void sendFinishLocation(Location location);
    void sendRideData(float distance, Duration duration);
    void saveLatLng(String where, LatLng latLng);
    void communicate(Long value);
}
