package com.example.uberapp_tim.model.message;

import com.example.uberapp_tim.model.ride.Ride;
import com.example.uberapp_tim.model.users.User;

import java.time.LocalDateTime;

public class Panic {
    private Long id;
    private User user;
    private Ride ride;
    private LocalDateTime time;
    private String reason;

    public Panic(){}

    public Panic(Long id, User user, Ride ride, LocalDateTime time, String reason) {
        this.id = id;
        this.user = user;
        this.ride = ride;
        this.time = time;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
