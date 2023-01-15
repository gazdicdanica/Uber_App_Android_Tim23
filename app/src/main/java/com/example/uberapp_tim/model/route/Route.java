package com.example.uberapp_tim.model.route;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Route {
    private Long id;
    private float distance;
    private Location departure;
    private Location destination;

    public Route(Long id, float distance, Location startLocation, Location endLocation) {
        this.id = id;
        this.distance = distance;
        this.departure = startLocation;
        this.destination = endLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
