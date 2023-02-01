package com.example.uberapp_tim.connection;

import static android.content.ContentValues.TAG;

import android.util.Log;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocket{

    public StompClient stompClient;

    public WebSocket(){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.0.20:8080/socket/websocket");
        stompClient.connect();
    }

    public void disconnect(){
        stompClient.disconnect();
    }
}
