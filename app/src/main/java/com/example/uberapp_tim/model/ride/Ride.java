package com.example.uberapp_tim.model.ride;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.example.uberapp_tim.dto.UserShortDTO;
import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.model.users.User;
import com.example.uberapp_tim.model.vehicle.CarType;
import com.example.uberapp_tim.model.vehicle.Vehicle;

public class Ride implements Serializable {
    private Long id;

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

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public UserShortDTO getDriver() {
        return driver;
    }

    public void setDriver(UserShortDTO driver) {
        this.driver = driver;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public boolean isPanic() {
        return isPanic;
    }

    public void setPanic(boolean panic) {
        isPanic = panic;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }

    public ArrayList<Route> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Route> locations) {
        this.locations = locations;
    }

    public ArrayList<UserShortDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<UserShortDTO> passengers) {
        this.passengers = passengers;
    }

    public CarType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(CarType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Rejection getRejection() {
        return rejection;
    }

    public void setRejection(Rejection rejection) {
        this.rejection = rejection;
    }

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private UserShortDTO driver;
    private int estimatedTimeInMinutes;
    private boolean isPanic;
    private Vehicle vehicle;
    private RideStatus rideStatus;
    private ArrayList<Route> locations;
    private ArrayList<UserShortDTO> passengers;
    private CarType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RideStatus status;
    private Rejection rejection;

}
