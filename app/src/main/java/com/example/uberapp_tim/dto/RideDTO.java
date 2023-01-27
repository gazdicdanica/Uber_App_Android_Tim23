package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.ride.Rejection;
import com.example.uberapp_tim.model.ride.RideStatus;
import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.model.vehicle.CarType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class RideDTO implements Serializable {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private UserShortDTO driver;
    private int estimatedTimeInMinutes;
    private ArrayList<Route> locations;
    private ArrayList<UserShortDTO> passengers;
    private CarType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RideStatus status;
    private Rejection rejection;

    public RideDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, double totalCost,
                   UserShortDTO driver, int estimatedTimeInMinutes, ArrayList<Route> locations,
                   ArrayList<UserShortDTO> passengers, CarType vehicleType, boolean babyTransport,
                   boolean petTransport, RideStatus status, Rejection rejection) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.status = status;
        this.rejection = rejection;
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

    @Override
    public String toString() {
        return "RideDTO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalCost=" + totalCost +
                ", driver=" + driver +
                ", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", locations=" + locations +
                ", passengers=" + passengers +
                ", vehicleType=" + vehicleType +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", status=" + status +
                ", rejection=" + rejection +
                '}';
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
}
