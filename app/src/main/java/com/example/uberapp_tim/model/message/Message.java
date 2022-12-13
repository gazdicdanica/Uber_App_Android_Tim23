package com.example.uberapp_tim.model.message;

import java.time.LocalDateTime;

import com.example.uberapp_tim.model.users.User;

public class Message {
    private int id;
    private String msgText;
    private LocalDateTime timeOfSending;
    private MessageType messageType;
    private User sender;
    private User receiver;

    public Message(int id, String msgText, LocalDateTime timeOfSending, MessageType messageType,
                   User receiver, User sender) {
        this.id = id;
        this.msgText = msgText;
        this.timeOfSending = timeOfSending;
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMsgText(){
        return this.msgText;
    }

    public User getSender(){
        return this.sender;
    }

}
