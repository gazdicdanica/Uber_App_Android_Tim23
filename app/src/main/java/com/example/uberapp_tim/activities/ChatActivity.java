package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.adapters.MessageAdapter;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler);
        mMessageAdapter = new MessageAdapter(this, new ArrayList<>());
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}