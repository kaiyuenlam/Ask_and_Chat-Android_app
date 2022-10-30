package com.example.askchat.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.askchat.R;

public class ChatFragment extends Fragment implements View.OnClickListener{
    TextView textViewContacts, textViewChats;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    FrameLayout frameLayoutContacts, frameLayoutChats;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        init(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        textViewContacts = view.findViewById(R.id.chat_fragment_contacts);
        textViewChats = view.findViewById(R.id.chat_fragment_chats);
        progressBar = view.findViewById(R.id.chat_fragment_progress_bar);
        recyclerView = view.findViewById(R.id.chat_fragment_recycler_view);
        frameLayoutContacts = view.findViewById(R.id.chat_fragment_fl_contacts);
        frameLayoutChats = view.findViewById(R.id.chat_fragment_fl_chats);

        textViewContacts.setOnClickListener(this);
        textViewChats.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_fragment_chats:
                textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
                frameLayoutChats.setBackgroundTintList(getContext().getColorStateList(R.color.logoColor));
                textViewContacts.setTextColor(Color.parseColor("#DF4F1A"));
                frameLayoutContacts.setBackgroundTintList(getContext().getColorStateList(R.color.white));
                break;

            case R.id.chat_fragment_contacts:
                textViewContacts.setTextColor(Color.parseColor("#FFFFFFFF"));
                frameLayoutContacts.setBackgroundTintList(getContext().getColorStateList(R.color.logoColor));
                textViewChats.setTextColor(Color.parseColor("#DF4F1A"));
                frameLayoutChats.setBackgroundTintList(getContext().getColorStateList(R.color.white));
                break;
        }
    }
}