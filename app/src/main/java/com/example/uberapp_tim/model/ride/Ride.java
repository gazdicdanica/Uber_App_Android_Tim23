package com.example.uberapp_tim.model.ride;

import java.time.LocalDateTime;

import com.example.uberapp_tim.model.vehicle.Vehicle;

public class Ride {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float totalPrice;
    private double estimatedTime;
    private boolean isPanic;
    private Vehicle vehicle;
    private RideStatus rideStatus;

    public Ride(Long id, LocalDateTime startTime, LocalDateTime endTime, float totalPrice,
                double estimatedTime, boolean isPanic, Vehicle vehicle,
                RideStatus rideStatus) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.estimatedTime = estimatedTime;
        this.isPanic = isPanic;
        this.vehicle = vehicle;
        this.rideStatus = rideStatus;
    }
}
