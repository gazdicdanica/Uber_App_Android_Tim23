package com.example.uberapp_tim.model.vehicle;

public class Vehicle {
    private Long id;
    private String model;
    private String registrationPlates;
    private int capacity;
    private boolean acceptBabies;
    private boolean acceptPets;
    private CarType carType;
    private float pricePerKilometer;

    public Vehicle(Long id, String model, String registrationPlates, int capacity,
                   boolean acceptBabies, boolean acceptPets, CarType carType, float pricePerKilometer) {
        this.id = id;
        this.model = model;
        this.registrationPlates = registrationPlates;
        this.capacity = capacity;
        this.acceptBabies = acceptBabies;
        this.acceptPets = acceptPets;
        this.carType = carType;
        this.pricePerKilometer = pricePerKilometer;
    }

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

    public String getRegistrationPlates() {
        return registrationPlates;
    }

    public void setRegistrationPlates(String registrationPlates) {
        this.registrationPlates = registrationPlates;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAcceptBabies() {
        return acceptBabies;
    }

    public void setAcceptBabies(boolean acceptBabies) {
        this.acceptBabies = acceptBabies;
    }

    public boolean isAcceptPets() {
        return acceptPets;
    }

    public void setAcceptPets(boolean acceptPets) {
        this.acceptPets = acceptPets;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public float getPricePerKilometer() {
        return pricePerKilometer;
    }

    public void setPricePerKilometer(float pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }
}
