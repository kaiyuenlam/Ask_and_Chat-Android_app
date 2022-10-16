package com.example.askchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.MainActivity;
import com.example.askchat.R;
import com.example.askchat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    View view;
    ImageView imageViewSearchQuestionButton;
    TextView textViewGreeting;
    EditText editTextSearchQuestion;
    RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initView();
        setupFirebase();
        changeTextViewGreeting();
    }

    private void initView() {
        editTextSearchQuestion = view.findViewById(R.id.search_bar);
        imageViewSearchQuestionButton = view.findViewById(R.id.mainActivity_searchBTN);
        textViewGreeting = view.findViewById(R.id.greeting);
        recyclerView = view.findViewById(R.id.mainActivity_recyclerView);

        imageViewSearchQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
                User userProfile = snapshot.getValue(User.class);

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

}