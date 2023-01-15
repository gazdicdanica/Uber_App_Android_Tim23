package com.example.uberapp_tim.model.route;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Route {
    private Long id;
    private double distance;
    private Location departure;
    private Location destination;

    public Route(Long id, double distance, Location startLocation, Location endLocation) {
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

    public double getDistance() {
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
