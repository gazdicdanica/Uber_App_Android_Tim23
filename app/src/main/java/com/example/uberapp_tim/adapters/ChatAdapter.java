package com.example.uberapp_tim.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.tools.Mokap;


public class ChatAdapter extends BaseAdapter {

    private Activity activity;

    public ChatAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return Mokap.getMessages().size();
    }

    @Override
    public Object getItem(int i) {
        return Mokap.getMessages().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Message message = Mokap.getMessages().get(i);
        View vi = view;
        if(view == null){
            vi = activity.getLayoutInflater().inflate(R.layout.inbox_item, null);
        }
        TextView name = (TextView)vi.findViewById(R.id.chat_name);
        TextView mess = (TextView)vi.findViewById(R.id.last_message);
        String senderName = message.getSender().getName() + " " + message.getSender().getLastName();
        name.setText(senderName);
        mess.setText(message.getMsgText());

        return vi;
    }
}
