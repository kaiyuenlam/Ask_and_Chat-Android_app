package com.example.askchat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.example.askchat.fragment.homefunc.AddNewPostActivity;
import com.example.askchat.fragment.homefunc.PostAdapter;
import com.example.askchat.fragment.homefunc.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;
    ImageView imageViewSearchQuestionButton, imageViewAddPostButton;
    TextView textViewGreeting;
    EditText editTextSearchQuestion;
    RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    PostAdapter postAdapter;
    List<PostModel> listPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setupFirebase();
        changeTextViewGreeting();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(View view) {
        editTextSearchQuestion = view.findViewById(R.id.search_bar);
        imageViewSearchQuestionButton = view.findViewById(R.id.mainActivity_searchBTN);
        imageViewAddPostButton = view.findViewById(R.id.mainActivity_addPostBTN);
        textViewGreeting = view.findViewById(R.id.greeting);
        recyclerView = view.findViewById(R.id.mainActivity_recyclerView);

        imageViewSearchQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imageViewAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity().getApplicationContext(), AddNewPostActivity.class));
            }
        });

        //set up recycler view
        listPost = new ArrayList<>();
        postAdapter = new PostAdapter(listPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(postAdapter);
        readPost();
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Users");
        userID = firebaseUser.getUid();
    }

    private void changeTextViewGreeting() {
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userProfile = snapshot.getValue(UserModel.class);

                if (userProfile != null) {
                    String userName = userProfile.getUserName();
                    textViewGreeting.setText("HI, " + userName + "!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    listPost.add(postModel);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}