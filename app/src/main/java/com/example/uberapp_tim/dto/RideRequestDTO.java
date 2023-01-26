package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.model.vehicle.CarType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideRequestDTO {
    Long id;
    private List<Route> locations = new ArrayList<>();
    private List<UserShortDTO> passengers = new ArrayList<>();
    private CarType vehicleType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double estimatedTime;
    private float estimatedPrice;
    private String scheduledTime;
    private boolean babyTransport;
    private boolean petTransport;

    public void addUser(UserShortDTO user) {
        this.passengers.add(user);
    }

    public void addRoute(Route r) {
        this.locations.add(r);
    }
    @Override
    public String toString() {
        return "RideRequestDTO{" +
                "locations=" + locations +
                ", passengers=" + passengers +
                ", vehicleType=" + vehicleType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", estimatedTime=" + estimatedTime +
                ", estimatedPrice=" + estimatedPrice +
                ", delayInMinutes=" + scheduledTime +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RideRequestDTO() {
    }

    public RideRequestDTO(List<Route> locations, List<UserShortDTO> passengers, CarType vehicleType,
                          LocalDateTime startTime, LocalDateTime endTime, double estimatedTime,
                          float estimatedPrice, String scheduledTime, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.estimatedTime = estimatedTime;
        this.estimatedPrice = estimatedPrice;
        this.scheduledTime = scheduledTime;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public List<Route> getLocations() {
        return locations;
    }

    public void setLocations(List<Route> locations) {
        this.locations = locations;
    }

    public List<UserShortDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserShortDTO> passengers) {
        this.passengers = passengers;
    }

    public CarType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(CarType vehicleType) {
        this.vehicleType = vehicleType;
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

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
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


}
