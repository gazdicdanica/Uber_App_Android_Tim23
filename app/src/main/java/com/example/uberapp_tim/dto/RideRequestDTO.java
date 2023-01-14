package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.model.vehicle.CarType;

import java.util.ArrayList;
import java.util.List;

public class RideRequestDTO {
    private List<Route> locations = new ArrayList<>();
    private List<UserShortDTO> passengers = new ArrayList<>();
    private CarType vehicleType;
    private int delayInMinutes;
    private boolean babyTransport;
    private boolean petTransport;

    public RideRequestDTO() {}

    public RideRequestDTO(List<Route> locations, List<UserShortDTO> passengers, CarType vehicleType,
                          int delayInMinutes, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.delayInMinutes = delayInMinutes;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public void addUser(UserShortDTO user) {
        this.passengers.add(user);
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

    public int getDelayInMinutes() {
        return delayInMinutes;
    }

    public void setDelayInMinutes(int delayInMinutes) {
        this.delayInMinutes = delayInMinutes;
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
