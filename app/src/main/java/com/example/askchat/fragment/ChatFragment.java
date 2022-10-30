package com.example.askchat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener{
    TextView textViewContacts, textViewChats;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    FrameLayout frameLayoutContacts, frameLayoutChats;
    FloatingActionButton fab_addFriendButton;

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
        fab_addFriendButton = view.findViewById(R.id.chat_fragment_fab);

        textViewContacts.setOnClickListener(this);
        textViewChats.setOnClickListener(this);
        fab_addFriendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_fragment_chats:
                textViewChats.setTextColor(Color.parseColor("#DF4F1A"));
                frameLayoutChats.setBackgroundTintList(getContext().getColorStateList(R.color.white));
                textViewContacts.setTextColor(Color.parseColor("#FFFFFFFF"));
                frameLayoutContacts.setBackgroundTintList(getContext().getColorStateList(R.color.logoColor));
                fab_addFriendButton.setVisibility(View.GONE);
                break;

            case R.id.chat_fragment_contacts:
                textViewContacts.setTextColor(Color.parseColor("#DF4F1A"));
                frameLayoutContacts.setBackgroundTintList(getContext().getColorStateList(R.color.white));
                textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
                frameLayoutChats.setBackgroundTintList(getContext().getColorStateList(R.color.logoColor));
                fab_addFriendButton.setVisibility(View.VISIBLE);
                break;

            case R.id.chat_fragment_fab:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                final EditText editText = new EditText(getActivity().getApplicationContext());

                builder.setView(editText);
                builder.setTitle("Input Email");
                builder.setPositiveButton("Add Friends", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addFriendsButton(editText.getText().toString().trim());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                break;
        }
    }

    private void addFriendsButton(String email) {
        if (email.isEmpty()) {
            Toast.makeText(requireActivity(), "Empty Input!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireActivity(), "Please provide valid email!", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);

                    if (email.equals(userModel.getEmail())) {
                        //add user to list
                        storeFriendListToFirebase(userModel.getuid());
                        Toast.makeText(requireActivity(), "Added successfully", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storeFriendListToFirebase(String FriendUID) {
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FriendUID).setValue(false);
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FriendUID).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(false);
    }
}