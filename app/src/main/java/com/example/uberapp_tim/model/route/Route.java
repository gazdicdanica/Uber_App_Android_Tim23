package com.example.uberapp_tim.model.route;

import java.time.LocalDateTime;

public class Route {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float distance;
    private double estimatedTime;
    private float estimatedPrice;
    private Location departure;
    private Location destination;

    public Route(Long id, LocalDateTime startTime, LocalDateTime endTime, float distance,
                 double estimatedTime, float estimatedPrice, Location departure, Location destination) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.estimatedPrice = estimatedPrice;
        this.departure = departure;
        this.destination = destination;
    }

    public Route() {}

    @Override
    public String toString() {
        return "Route{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", distance=" + distance +
                ", estimatedTime=" + estimatedTime +
                ", estimatedPrice=" + estimatedPrice +
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public float getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(float estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
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
