package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.adapters.MessageAdapter;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.dto.SendMessageDTO;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.model.message.MessageType;
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

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        send = findViewById(R.id.button_chat_send);
        editText = findViewById(R.id.edit_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessageDTO dto = new SendMessageDTO(editText.getText().toString(), MessageType.RIDE, rideId);
                editText.setText("");
                ServiceUtils.userService.sendMessage(user2id, dto).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
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

                            MessageDTO m = g.fromJson(message, MessageDTO.class);
                            messages.add(m);
                            mMessageRecycler.setAdapter(new MessageAdapter(ChatActivity.this, messages));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        String idStr = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null);
        user1id = Long.valueOf(idStr);

        user2id = getIntent().getLongExtra("userId", 0);
        rideId = getIntent().getLongExtra("rideId", 0);


        mHandler = new Handler(Looper.getMainLooper());
        startRepeat();

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    Runnable mStatusCheker = new Runnable() {
        @Override
        public void run() {
            ServiceUtils.userService.getMessagesForUsersByRide(user2id, user1id, rideId).enqueue(new Callback<ResponseBody>() {
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
                        mMessageRecycler.setAdapter(new MessageAdapter(ChatActivity.this, messages));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopRepeat();
    }

    public void startRepeat(){
        mStatusCheker.run();
        mHandler.postDelayed(mStatusCheker, 5000);
    }

    public void stopRepeat(){
        mHandler.removeCallbacks(mStatusCheker);
    }
}