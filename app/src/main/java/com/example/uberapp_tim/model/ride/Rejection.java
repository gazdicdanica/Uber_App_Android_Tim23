package com.example.uberapp_tim.model.ride;

import com.example.uberapp_tim.model.users.User;

import java.time.LocalDateTime;

public class Rejection {
    public Long id;
    public Ride ride;
    public String reason;
    public User user;
    public LocalDateTime time;
}
