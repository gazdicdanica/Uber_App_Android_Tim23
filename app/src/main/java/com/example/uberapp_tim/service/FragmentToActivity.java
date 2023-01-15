package com.example.uberapp_tim.service;


import com.example.uberapp_tim.model.route.Location;

public interface FragmentToActivity {
    void communicate(String msg);
    void sendStartLocation(Location location);
    void sendFinishLocation(Location location);
}