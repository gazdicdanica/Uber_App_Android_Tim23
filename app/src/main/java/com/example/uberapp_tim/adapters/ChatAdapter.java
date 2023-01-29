package com.example.uberapp_tim.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.ChatActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.tools.Mokap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private Map<Long, List<MessageDTO>> data;
    private List<Long> keys = new ArrayList<>();

    public ChatAdapter(Activity activity, Map<Long, List<MessageDTO>> data){
        this.activity = activity;
        this.data = data;
        keys.addAll(data.keySet());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_item, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Long key = keys.get(position);
        List<MessageDTO> li = data.get(key);
        MessageDTO message = (MessageDTO)li.get(li.size()-1);
        ((ChatHolder) holder).bind(message);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ChatActivity.class);
                i.putExtra("data", (Serializable) li);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        Log.wtf("size", String.valueOf(data.size()));
        return this.keys.size();
    }

    private class ChatHolder extends RecyclerView.ViewHolder{
        TextView fullName, lastMessage;

        ChatHolder(View itemView){
            super(itemView);

            fullName = (TextView) itemView.findViewById(R.id.chat_name);
            lastMessage = (TextView) itemView.findViewById(R.id.last_message);

        }

        void bind(MessageDTO messageDTO){
            String s;
            String m;
            if(String.valueOf(messageDTO.getSender().getId()).equals(activity.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null))){
                s = messageDTO.getReceiver().getName()+ " " + messageDTO.getReceiver().getLastName();
                m = "You: " + messageDTO.getMessage();
            }else{
                s = messageDTO.getSender().getName() + " " + messageDTO.getSender().getLastName();
                m = messageDTO.getMessage();
            }
            lastMessage.setText(m);
            fullName.setText(s);
        }
    }
}
