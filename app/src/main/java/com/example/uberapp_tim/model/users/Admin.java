package com.example.uberapp_tim.model.users;

import android.graphics.Bitmap;

public class Admin extends User{
    public Admin(int id, String name, String lastName, String email, String phoneNumber, String address, String password, Bitmap profilePhoto, boolean blocked){
        super(id, name, lastName, email, phoneNumber, address, password, profilePhoto, blocked);
    }
}
