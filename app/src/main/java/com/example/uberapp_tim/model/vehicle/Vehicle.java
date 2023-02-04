package com.example.uberapp_tim.model.vehicle;

import com.example.uberapp_tim.model.route.Location;

public class Vehicle {
    private Long id;
    private String model;
    private String licenseNumber;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;
    private VehicleType vehicleType;
    private Location currentLocation;


    public Vehicle(Long id, String model, String licenseNumber, int passengerSeats,
                   boolean babyTransport, boolean petTransport, VehicleType vehicleType, Location currentLocation) {
        this.id = id;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.vehicleType = vehicleType;
        this.currentLocation = currentLocation;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", registrationPlates='" + licenseNumber + '\'' +
                ", capacity=" + passengerSeats +
                ", acceptBabies=" + babyTransport +
                ", acceptPets=" + petTransport +
                ", carType=" + vehicleType.getType() +
                '}';
    }

    public Vehicle() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(int passengerSeats) {
        this.passengerSeats = passengerSeats;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
