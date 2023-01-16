package com.example.uberapp_tim.model.route;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Route {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float distance;
    private double estimatedTime;
    private float estimatedPrice;
    private Location startLocation;
    private Location endLocation;

    @Override
    public String toString() {
        return "Route{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", distance=" + distance +
                ", estimatedTime=" + estimatedTime +
                ", estimatedPrice=" + estimatedPrice +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                '}';
    }

    public Route(Long id, LocalDateTime startTime, LocalDateTime endTime, float distance,
                 double estimatedTime, float estimatedPrice, Location startLocation,
                 Location endLocation) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.estimatedPrice = estimatedPrice;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
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

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Route() {}
}
