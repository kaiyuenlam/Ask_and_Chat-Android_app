package com.example.askchat.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.R;
import com.example.askchat.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    String TAG = "Sign Up Activity";

    private EditText editTextUserName, editTextEmail, editTextPassword;
    private Button buttonCreateAC;
    private TextView textViewToSignin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();  //action bar hidden
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        bindView();
        setOnClickListener();
    }

    private void bindView() {
        editTextUserName = findViewById(R.id.username_signup);
        editTextEmail = findViewById(R.id.email_signup);
        editTextPassword = findViewById(R.id.password_signup);
        buttonCreateAC = findViewById(R.id.signupBTN);
        textViewToSignin = findViewById(R.id.toSignin);
    }

    private void setOnClickListener() {
        textViewToSignin.setOnClickListener(this);
        buttonCreateAC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupBTN:
                registerUser();
                hideSoftKeyBoard();
                break;

            case R.id.toSignin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String userName = editTextUserName.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (userName.isEmpty()) {
            editTextUserName.setError("User name is required!");
            editTextUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //register successfully
                            User user = new User(userName, email);
                            Log.d(TAG, "Sign up successfully");
                            //Send user object to real time database
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                //Store in database successfully
                                                Toast.makeText(SignUpActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                //redirect to login activity
                                                finish();
                                            } else {
                                                //Failed to store in database
                                                Toast.makeText(SignUpActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}