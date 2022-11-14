package com.example.askchat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.askchat.fragment.chatfunc.ChatActivity;
import com.example.askchat.fragment.chatfunc.ContactsAdapter;
import com.example.askchat.fragment.chatfunc.RecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener
        , ContactsAdapter.AcceptedAdapter, ContactsAdapter.RemoveItem, RecyclerItemTouchHelper.RemoveItemConfirm
        , ContactsAdapter.ToChatListener{

    TextView textViewContacts, textViewChats, textViewFriendsRequest, textViewFriends;
    ProgressBar progressBar;
    RecyclerView recyclerViewContacts, recyclerViewFriendRequest, recyclerViewChats;
    FrameLayout frameLayoutContacts, frameLayoutChats, frameLayoutContactLayout, frameLayoutChatsLayout;
    FloatingActionButton fab_addFriendButton;

    List<String> listFriendsRequest, listContacts, listChatRoom;

    ContactsAdapter friendsRequestAdapter, contactsAdapter;

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
        recyclerViewContacts = view.findViewById(R.id.chat_fragment_recycler_view);
        frameLayoutContacts = view.findViewById(R.id.chat_fragment_fl_contacts);
        frameLayoutChats = view.findViewById(R.id.chat_fragment_fl_chats);
        fab_addFriendButton = view.findViewById(R.id.chat_fragment_fab);
        recyclerViewFriendRequest = view.findViewById(R.id.chat_fragment_friend_request_recycler_view);
        textViewFriends = view.findViewById(R.id.chat_fragment_friends_textview);
        textViewFriendsRequest = view.findViewById(R.id.chat_fragment_friend_request_textView);
        frameLayoutContactLayout = view.findViewById(R.id.chat_fragment_contacts_layout);
        frameLayoutChatsLayout = view.findViewById(R.id.chat_fragment_chats_layout);
        recyclerViewChats = view.findViewById(R.id.chat_fragment_chats_recycler_view);

        textViewContacts.setOnClickListener(this);
        textViewChats.setOnClickListener(this);
        fab_addFriendButton.setOnClickListener(this);

        listFriendsRequest = new ArrayList<>();
        listChatRoom = new ArrayList<>();
        listContacts = new ArrayList<>();

        friendsRequestAdapter = new ContactsAdapter(listFriendsRequest, this, this);
        contactsAdapter = new ContactsAdapter(this, listContacts, this);
        recyclerViewFriendRequest.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewFriendRequest.setAdapter(friendsRequestAdapter);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewContacts.setAdapter(contactsAdapter);
        readFriends();

        ItemTouchHelper contactsTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelper(contactsAdapter, getActivity().getApplicationContext(), this));
        contactsTouchHelper.attachToRecyclerView(recyclerViewContacts);
        ItemTouchHelper friendRequestTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelper(friendsRequestAdapter, getActivity().getApplicationContext(), this));
        friendRequestTouchHelper.attachToRecyclerView(recyclerViewFriendRequest);
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
                frameLayoutContactLayout.setVisibility(View.GONE);
                frameLayoutChatsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.chat_fragment_contacts:
                textViewContacts.setTextColor(Color.parseColor("#DF4F1A"));
                frameLayoutContacts.setBackgroundTintList(getContext().getColorStateList(R.color.white));
                textViewChats.setTextColor(Color.parseColor("#FFFFFFFF"));
                frameLayoutChats.setBackgroundTintList(getContext().getColorStateList(R.color.logoColor));
                fab_addFriendButton.setVisibility(View.VISIBLE);
                frameLayoutContactLayout.setVisibility(View.VISIBLE);
                frameLayoutChatsLayout.setVisibility(View.GONE);
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
        // 1= send friend request, 2= receive friend request, 3= we are friend
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FriendUID).setValue(1);
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FriendUID).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(2);
    }

    private void readFriends() {
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listContacts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.getValue().toString().equals("2")) {
                                listFriendsRequest.add(dataSnapshot.getKey());
                            } else if (dataSnapshot.getValue().toString().equals("3")) {
                                listContacts.add(dataSnapshot.getKey());
                            }
                        }
                        contactsAdapter.notifyDataSetChanged();
                        friendsRequestAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        if (listFriendsRequest.size() == 0) {
            textViewFriendsRequest.setVisibility(View.VISIBLE);
            recyclerViewFriendRequest.setVisibility(View.VISIBLE);
        } else {
            textViewFriendsRequest.setVisibility(View.GONE);
            recyclerViewFriendRequest.setVisibility(View.GONE);
        }
    }

    @Override
    public void accepted(boolean isFriends, int position) {
        String friendID = listFriendsRequest.get(position);
        if (isFriends) {
            //accept friend request
            FirebaseDatabase.getInstance().getReference("Friends")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(friendID).setValue(3);
            FirebaseDatabase.getInstance().getReference("Friends")
                    .child(friendID)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(3);
        } else {
            FirebaseDatabase.getInstance().getReference("Friends")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(friendID).setValue(2);
            FirebaseDatabase.getInstance().getReference("Friends")
                    .child(friendID)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(1);
            listFriendsRequest.clear();
        }
    }

    @Override
    public void remove(boolean isContact, int position) {
        String friendUID;
        if (isContact) {
            friendUID = listContacts.get(position);
        } else {
            friendUID = listFriendsRequest.get(position);
        }

        FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(friendUID).removeValue();
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(friendUID)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        listFriendsRequest.clear();
    }

    @Override
    public void callDialog(ContactsAdapter adapter, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Delete card");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteItem(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.notifyItemChanged(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void toChatActivity(int position) {
        String userID = listContacts.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
        intent.putExtra("friendID", userID);
        getActivity().startActivity(intent);
    }
}