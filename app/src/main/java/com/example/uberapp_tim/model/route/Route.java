package com.example.uberapp_tim.model.route;

import java.time.LocalDateTime;

public class Route {
    private Long id;
    private double distance;
    private Location departure;
    private Location destination;

    public Route(Long id, double distance, Location departure, Location destination) {
        this.id = id;
        this.distance = distance;
        this.departure = departure;
        this.destination = destination;
    }

    public Route() {}

    @Override
    public String toString() {
        return "Route{" +
                ", distance=" + distance +
                ", startLocation=" + departure +
                ", endLocation=" + destination +
                '}';
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

    public void setDistance(double distance) {
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
