package com.example.uberapp_tim.model.ride;

import com.example.uberapp_tim.model.users.User;

import java.time.LocalDateTime;

public class Rejection {
    public Long id;
    public Ride ride;
    public String reason;
    public User user;
    public LocalDateTime time;

    public Rejection(){}

    public Rejection(Long id, Ride ride, String reason, User user, LocalDateTime time) {
        this.id = id;
        this.ride = ride;
        this.reason = reason;
        this.user = user;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
