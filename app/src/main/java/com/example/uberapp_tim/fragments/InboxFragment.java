package com.example.uberapp_tim.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim.R;

import com.example.uberapp_tim.adapters.ChatAdapter;
import com.example.uberapp_tim.adapters.MessageAdapter;

import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.tools.Mokap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends ListFragment {



    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data){
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.inbox, vg, false);

    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id){
        super.onListItemClick(l, view, position, id);

    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);


    }

}
