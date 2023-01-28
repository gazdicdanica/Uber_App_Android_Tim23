package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.message.MessageType;

public class SendMessageDTO {
    private String message;
    private MessageType type;
    private Long rideId;


    public SendMessageDTO(String message, MessageType messageType, Long rideId) {
        this.message = message;
        this.type = messageType;
        this.rideId = rideId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return type;
    }

    public void setMessageType(MessageType messageType) {
        this.type = messageType;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}
