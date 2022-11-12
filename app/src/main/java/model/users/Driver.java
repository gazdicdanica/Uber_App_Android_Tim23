package model.users;

import android.graphics.Bitmap;

public class Driver extends User{
    private Bitmap driverLicence;
    private Bitmap registration;
    private boolean isActive;

    public Driver(int id, String name, String email, String lastName, String phoneNumber,
                  String address, String password, Bitmap profilePhoto, boolean blocked,
                  Bitmap driverLicence, Bitmap registration, boolean isActive){

        super(id, name, lastName, email, phoneNumber, address, password, profilePhoto, blocked);
        this.driverLicence = driverLicence;
        this.registration = registration;
        this.isActive = isActive;
    }

    public Bitmap getRegistration() {
        return registration;
    }

    public void setRegistration(Bitmap registration) {
        this.registration = registration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Bitmap getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(Bitmap driverLicence) {
        this.driverLicence = driverLicence;
    }

}
