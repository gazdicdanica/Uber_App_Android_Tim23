package com.example.uberapp_tim.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.adapters.ChatAdapter;

import com.example.uberapp_tim.model.message.Message;
import com.example.uberapp_tim.tools.Mokap;

public class InboxFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data){
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.inbox, vg, false);

    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id){
        super.onListItemClick(l, view, position, id);

        Message message = Mokap.getMessages().get(position);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        ChatAdapter adapter = new ChatAdapter(getActivity());
        setListAdapter(adapter);
    }
}
