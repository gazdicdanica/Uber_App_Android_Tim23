package com.example.uberapp_tim.model.users;

import android.graphics.Bitmap;

import com.example.uberapp_tim.model.route.Route;

import java.io.Serializable;

public class Passenger extends User implements Serializable {

    private Route favoriteRoute;

    public Passenger(Long id, String name, String lastName, String email, String phoneNumber,
                     String address, String password, Bitmap profilePhoto,
                     boolean blocked, Route favoriteRoute){
        super(id, name, lastName, email, phoneNumber, address, password, profilePhoto, blocked);

        this.favoriteRoute = favoriteRoute;
    }

}
