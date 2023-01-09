package com.example.uberapp_tim.activities.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim.R;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import model.message.Message;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;

    public MessageAdapter(Context context, List<Message> messages){
        mContext = context;
        mMessageList = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == VIEW_TYPE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_messages, parent, false);
            return new SentMessageHolder(view);
        }else if (viewType == VIEW_TYPE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_messages, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

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
        Message message = (Message) mMessageList.get(position);

        // TODO resolve logged in user
        if (message.getSender().getId() == 1){
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
            messageTxt = (TextView) itemView.findViewById(R.id.text_user_other);
            timeTxt = (TextView) itemView.findViewById(R.id.text_timestamp_other);
            nameTxt = (TextView) itemView.findViewById(R.id.text_message_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_profile_other);
            dateTxt = (TextView) itemView.findViewById(R.id.text_date_other);
        }

        void bind(Message message){
            messageTxt.setText(message.getMsgText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("HH:mm")));
                dateTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            }
            String fullName = message.getSender().getName() + " " + message.getSender().getLastName();
            nameTxt.setText(fullName);

            // TODO set image
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageTxt, timeTxt, dateTxt;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageTxt = (TextView) itemView.findViewById(R.id.text_message_me);
            timeTxt = (TextView) itemView.findViewById(R.id.text_timestamp_me);
        }

        void bind(Message message) {
            messageTxt.setText(message.getMsgText());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("HH:mm")));
                dateTxt.setText(message.getTimeOfSending().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            }
        }
    }

}
