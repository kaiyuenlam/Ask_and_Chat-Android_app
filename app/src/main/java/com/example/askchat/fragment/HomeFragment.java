package com.example.askchat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.example.askchat.fragment.homefunc.AddNewPostActivity;
import com.example.askchat.fragment.homefunc.PostActivity;
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

public class HomeFragment extends Fragment implements PostAdapter.OnPostClickListener, PostAdapter.OnPostLongClickListener{

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

    FavorPostDatabaseHelper databaseHelper;

    MyFavorListListener myFavorListListener;

    public HomeFragment(FavorPostDatabaseHelper databaseHelper, MyFavorListListener myFavorListListener) {
        this.databaseHelper = databaseHelper;
        this.myFavorListListener = myFavorListListener;
    }

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
                //search();
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
        postAdapter = new PostAdapter(listPost, this, this, databaseHelper.getAllPost());
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
                    listPost.add(0, postModel);
                }
                postAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void OnPostClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), PostActivity.class);
        intent.putExtra("postID", listPost.get(position).getPostID());
        startActivity(intent);
    }

    @Override
    public void OnPostLongClick(int position) {
        String postID = listPost.get(position).getPostID();
        List<String> favorList = databaseHelper.getAllPost();
        boolean isFavor = false;
        for (int i = 0; i < favorList.size(); i++) {
            if (postID.equals(favorList.get(i))) {
                isFavor = true;
                break;
            }
        }

        if (isFavor) {
            databaseHelper.deletePost(postID);
        } else {
            databaseHelper.insertPost(postID);
        }
        myFavorListListener.notifyDatabaseChanged();
    }

    private void search() {
        String searchContent = editTextSearchQuestion.getText().toString().trim();

        if (searchContent.isEmpty()) {
            editTextSearchQuestion.requestFocus();
        } else {
            List<PostModel> searchResult = new ArrayList<>();
            for (int i = 0; i < listPost.size(); i++) {
                if (listPost.get(i).getQuestion().contains(searchContent)) {
                    searchResult.add(listPost.get(i));
                }
            }
            listPost.clear();
            listPost = searchResult;
            postAdapter.notifyDataSetChanged();
        }
    }
}