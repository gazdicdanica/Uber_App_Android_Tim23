package com.example.uberapp_tim.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim.R;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.model.users.User;


public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context mContext;
    private List<MessageDTO> mMessageList;
    private User receiving;

    public MessageAdapter(Context context, List<MessageDTO> messages, User receiving){
        mContext = context;
        mMessageList = messages;
        this.receiving = receiving;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == VIEW_TYPE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_messages, parent, false);
            return new SentMessageHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_messages, parent, false);
            return new ReceivedMessageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDTO message = (MessageDTO) mMessageList.get(position);

        switch (holder.getItemViewType()){
            case VIEW_TYPE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position){
        MessageDTO message = (MessageDTO) mMessageList.get(position);

        String idStr = mContext.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null);
        Long id = Long.parseLong(idStr);
        Log.d("SENT_ID", idStr);
        if (Objects.equals(message.getSender(), id)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageTxt, timeTxt, nameTxt, dateTxt;
        ImageView profileImage;

        protected  ReceivedMessageHolder(View itemView){
            super(itemView);
            messageTxt = (TextView) itemView.findViewById(R.id.text_message_other);
            timeTxt = (TextView) itemView.findViewById(R.id.text_timestamp_other);
            nameTxt = (TextView) itemView.findViewById(R.id.name_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_profile_other);
            dateTxt = (TextView) itemView.findViewById(R.id.text_date_other);
        }

        void bind(MessageDTO message){
            messageTxt.setText(message.getMessage());
            String fullName = receiving.getName() + " " + receiving.getLastName();
            nameTxt.setText(fullName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("HH:mm")));
                dateTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            }
//            String fullName = message.getSender().getName() + " " + message.getSender().getLastName();
//            nameTxt.setText(fullName);

            // TODO set image
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageTxt, timeTxt, dateTxt;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageTxt = (TextView) itemView.findViewById(R.id.text_message_me);
            timeTxt = (TextView) itemView.findViewById(R.id.text_timestamp_me);
            dateTxt = (TextView) itemView.findViewById(R.id.text_date_me);
        }

        void bind(MessageDTO message) {
            messageTxt.setText(message.getMessage());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("HH:mm")));
                dateTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            }
        }
    }

}
