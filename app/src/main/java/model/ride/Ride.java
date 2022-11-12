package model.ride;

import java.time.LocalDateTime;

import model.payment.Payment;
import model.vehicle.Vehicle;

public class Ride {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float totalPrice;
    private double estimatedTime;
    private boolean isPanic;
    private Vehicle vehicle;
    private RideStatus rideStatus;
    private Payment payment;

    public Ride(int id, LocalDateTime startTime, LocalDateTime endTime, float totalPrice,
                double estimatedTime, boolean isPanic, Vehicle vehicle,
                RideStatus rideStatus, Payment payment) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.estimatedTime = estimatedTime;
        this.isPanic = isPanic;
        this.vehicle = vehicle;
        this.rideStatus = rideStatus;
        this.payment = payment;
    }
}
