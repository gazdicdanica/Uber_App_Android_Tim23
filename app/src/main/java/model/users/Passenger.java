package model.users;

import android.graphics.Bitmap;

import model.route.Route;

public class Passenger extends User{

    private Route favoriteRoute;

    public Passenger(int id, String name, String email, String lastName, String phoneNumber,
                     String address, String password, Bitmap profilePhoto,
                     boolean blocked, Route favoriteRoute){
        super(id, name, lastName, email, phoneNumber, address, password, profilePhoto, blocked);

        this.favoriteRoute = favoriteRoute;
    }

}
