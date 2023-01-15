package com.example.uberapp_tim.service;

import com.example.uberapp_tim.model.route.Location;

public interface ActivityToFragment {

    void sendDestination(Location location);
    void sendDeparture(Location location);
}
