package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.message.MessageType;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long sender;
    private Long receiver;
    private String message;

    private LocalDateTime timeOfSending;
    private MessageType type;
    private Long ride;


    public MessageDTO(Long id, Long sender, Long receiver, String message, LocalDateTime timeOfSending, MessageType type, Long ride) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timeOfSending = timeOfSending;
        this.type = type;
        this.ride = ride;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(LocalDateTime timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Long getRide() {
        return ride;
    }

    public void setRide(Long ride) {
        this.ride = ride;
    }
}
