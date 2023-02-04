package com.example.uberapp_tim.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.uberapp_tim.connection.WebSocket;

import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service {

    private WebSocket webSocket;
    private OnMessageReceivedListener listener;

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener){
        this.listener = listener;
    }

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.webSocket = new WebSocket();
        String rideId = intent.getStringExtra("rideId");
        this.webSocket.stompClient.topic("/messages/"+rideId).subscribe(topicMessage ->{
            if(listener != null){
                listener.onMessageReceived(topicMessage.getPayload());
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
