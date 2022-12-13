package com.example.uberapp_tim.model;

import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.model.users.User;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    List<Message> messages;
    User person;

    public Chat(){
        messages = new ArrayList<Message>();
    }

    public void setPerson(User recipiant){
        this.person = recipiant;
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
    }

    public void addMessage(Message m){
        this.messages.add(m);
    }
}
