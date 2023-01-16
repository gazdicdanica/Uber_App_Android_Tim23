package com.example.uberapp_tim.dto;

import com.example.uberapp_tim.model.message.MessageType;

public class SendMessageDTO {
    private String message;
    private MessageType messageType;
    private Long rideId;


    public SendMessageDTO(String message, MessageType messageType, Long rideId) {
        this.message = message;
        this.messageType = messageType;
        this.rideId = rideId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}
