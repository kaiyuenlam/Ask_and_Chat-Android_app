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

import com.example.askchat.R;

public class HomeFragment extends Fragment {

    View view;
    ImageView imageViewSearchQuestionButton;
    TextView textViewGreeting;
    EditText editTextSearchQuestion;
    RecyclerView recyclerView;

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


}