package com.example.uberapp_tim.model.message;

import com.example.uberapp_tim.model.users.User;

import java.time.LocalDateTime;

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
