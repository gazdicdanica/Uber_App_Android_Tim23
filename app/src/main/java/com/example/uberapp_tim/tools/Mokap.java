package com.example.uberapp_tim.tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.model.message.MessageType;
import com.example.uberapp_tim.model.users.Driver;
import com.example.uberapp_tim.model.users.Passenger;
import com.example.uberapp_tim.model.users.User;

public class Mokap {

    public static List<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        User u1 = new Driver((long)1, "Milos", "Obradovic", "test@gmail.com", "0691231234", "Maksima Gorkog 57", "1234", null, false, null, null, false);
        User u2 = new Passenger((long)2, "Danica", "Gazdic", "test2@gmail.com", "0691852001", "JNA 12", "danica", null, false, null);

        users.add(u1);
        users.add(u2);

        return users;
    }


    public static List<Message> getMessages(){

        User u1 = new Driver((long)1, "Milos", "Obradovic", "test@gmail.com", "0691231234", "Maksima Gorkog 57", "1234", null, false, null, null, false);
        User u2 = new Passenger((long)2, "Danica", "Gazdic", "test2@gmail.com", "0691852001", "JNA 12", "danica", null, false, null);
        User u3 = new Passenger((long)3, "Jovan", "Jovanovic", "test3@gmail.com", "0651832245", "Bulevar Oslobodjenja 2", "1111", null, false, null);

        ArrayList<Message> mess = new ArrayList<Message>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Message m = new Message((long)1, "Hej", LocalDateTime.now(), MessageType.RIDE, u1, u2);
            Message m1 = new Message((long)2, "CAO", LocalDateTime.now(), MessageType.RIDE, u1, u3);
            Message m2 = new Message((long)3, "CAO", LocalDateTime.now(), MessageType.RIDE, u1, u2);
            mess.add(m);
            mess.add(m1);
            mess.add(m2);

        }

        return mess;
    }
}
