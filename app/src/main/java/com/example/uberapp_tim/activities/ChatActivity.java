package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.adapters.MessageAdapter;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.dto.SendMessageDTO;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.model.message.MessageType;
import com.example.uberapp_tim.model.users.User;
import com.example.uberapp_tim.receiver.NotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;

    ImageButton send;
    EditText editText;

    private List<MessageDTO> messages = new ArrayList<>();
    private Long rideId;
    private Long user1id;
    private Long user2id;
    private WebSocket webSocket;

    private String jwt;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webSocket = new WebSocket();
        setContentView(R.layout.chat);
        jwt = "Bearer "+ getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");

        send = findViewById(R.id.button_chat_send);
        editText = findViewById(R.id.edit_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessageDTO dto = new SendMessageDTO(editText.getText().toString(), MessageType.RIDE, rideId);
                editText.setText("");
                ServiceUtils.userService.sendMessage(jwt, user2id, dto).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.code() == 200){
                                String message = response.body().string();
                                Gson g = null;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                                        @Override
                                        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                            return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                                        }
                                    }).create();
                                }

                                Log.wtf("POLSATO", "TRUE");

                                MessageDTO m = g.fromJson(message, MessageDTO.class);
                                messages.add(m);
                                mMessageRecycler.getAdapter().notifyDataSetChanged();
//                                mMessageRecycler.setAdapter(new MessageAdapter(ChatActivity.this, messages));
                            }else{
                                Log.wtf("RESPONSE", response.body().toString());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.wtf("FAILURE", t.getMessage());
                    }
                });
            }
        });

        String idStr = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null);
        user1id = Long.valueOf(idStr);

        user2id = getIntent().getLongExtra("userId", 0);
        rideId = getIntent().getLongExtra("rideId", 0);

        Log.wtf("user id", user1id.toString());
        Log.wtf("ride id", rideId.toString());
        webSocket.stompClient.topic("/message/"+rideId.toString()+"/"+user1id.toString()).subscribe(topicMessage ->{
            String message = topicMessage.getPayload();
            Log.d("MESSAGE", message);

            Gson g = null;

            g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                }
            }).create();

            MessageDTO messageDTO = g.fromJson(message, MessageDTO.class);
            Log.wtf("converted", messageDTO.toString());
            Intent i = new Intent(this, NotificationReceiver.class);
            i.putExtra("title", "In ride message");
            i.putExtra("text", messageDTO.getMessage());
            i.putExtra("channel", "in_ride_channel");
            i.putExtra("id", getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null));

            Log.d("BEFORE BROADCAS", "");
            sendBroadcast(i);

            messages.add(messageDTO);
//            mMessageRecycler.setAdapter(new MessageAdapter(ChatActivity.this, messages));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMessageRecycler.getAdapter().notifyDataSetChanged();
                }
            });

            Log.d("AFTER RECYCLER", "");
        });

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceUtils.userService.getMessagesForUsersByRide(jwt, user2id, user1id, rideId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String message = response.body().string();
                    Gson g = null;

                    Toast.makeText(ChatActivity.this, "update", Toast.LENGTH_SHORT).show();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                            @Override
                            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                            }
                        }).create();
                    }

                    Type listType = new TypeToken<ArrayList<MessageDTO>>(){}.getType();
                    messages = g.fromJson(message, listType);

                    ServiceUtils.userService.getUserData(jwt, user2id).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.wtf("Response123", response.body().getName());
                            mMessageRecycler.setAdapter(new MessageAdapter(ChatActivity.this, messages, response.body()));
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}