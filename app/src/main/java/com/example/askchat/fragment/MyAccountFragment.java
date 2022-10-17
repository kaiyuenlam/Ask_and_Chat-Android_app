package com.example.askchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askchat.R;

public class MyAccountFragment extends Fragment {

    ImageView imageViewUserIcon;
    EditText editTextEmail, editTextName, editTextEducationLV, editTextCollege;
    TextView textViewSaveButton;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        imageViewUserIcon = view.findViewById(R.id.fragment_my_account_icon);
        editTextEmail = view.findViewById(R.id.fragment_my_account_email_input);
        editTextName = view.findViewById(R.id.fragment_my_account_user_name_input);
        editTextEducationLV = view.findViewById(R.id.fragment_my_account_education_input);
        editTextCollege = view.findViewById(R.id.fragment_my_account_college_input);
        textViewSaveButton = view.findViewById(R.id.fragment_my_account_saveBTN);
    }
}