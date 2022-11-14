package model.users;

import android.graphics.Bitmap;

import java.io.Serializable;

import model.route.Route;

public class Passenger extends User implements Serializable {

    private Route favoriteRoute;

    public Passenger(int id, String name, String lastName, String email, String phoneNumber,
                     String address, String password, Bitmap profilePhoto,
                     boolean blocked, Route favoriteRoute){
        super(id, name, lastName, email, phoneNumber, address, password, profilePhoto, blocked);

        this.favoriteRoute = favoriteRoute;
    }

}
