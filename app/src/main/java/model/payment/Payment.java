package model.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

import model.users.Passenger;

public class Payment {
    private int id;
    private ArrayList<Passenger> payers;
    private LocalDateTime timeOfPayment;
    private float totalPrice;
    private PaymentType paymentType;

    public Payment(int id, ArrayList<Passenger> payers, LocalDateTime timeOfPayment,
                   float totalPrice, PaymentType paymentType) {
        this.id = id;
        this.payers = payers;
        this.timeOfPayment = timeOfPayment;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
    }
}
