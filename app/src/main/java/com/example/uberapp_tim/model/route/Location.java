package com.example.uberapp_tim.model.route;

public class Location {
    private Long id;
    private double longitude;
    private double latitude;
    private String address;

    public Location(Long id, double longitude, double latitude, String address) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public Location(android.location.Location l){
        this.longitude = l.getLongitude();
        this.latitude = l.getLatitude();
    }

    public Location(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
