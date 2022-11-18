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

import com.example.askchat.R;
import com.example.askchat.fragment.homefunc.PostActivity;
import com.example.askchat.fragment.homefunc.PostAdapter;
import com.example.askchat.fragment.homefunc.PostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavorFragment extends Fragment implements PostAdapter.OnPostClickListener{
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<PostModel> listPost;
    FavorPostDatabaseHelper databaseHelper;

    public MyFavorFragment(FavorPostDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_favor, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        listPost = new ArrayList<>();
        recyclerView = view.findViewById(R.id.fragment_favor_recyclerView);
        readPost();
        postAdapter = new PostAdapter(listPost, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(postAdapter);
    }

    @Override
    public void OnPostClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), PostActivity.class);
        intent.putExtra("postID", listPost.get(position).getPostID());
        startActivity(intent);
    }


    public void notifyChanged() {
        readPost();
        postAdapter.notifyDataSetChanged();
    }

    private void readPost() {
        List<String> list = databaseHelper.getAllPost();
        listPost.clear();
        for (int i = 0; i < list.size(); i++) {
            FirebaseDatabase.getInstance().getReference("Posts").child(list.get(i))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listPost.add(snapshot.getValue(PostModel.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

}