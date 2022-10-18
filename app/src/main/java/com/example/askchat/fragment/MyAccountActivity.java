package com.example.askchat.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.MainActivity;
import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.example.askchat.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyAccountActivity extends AppCompatActivity {

    ImageView imageViewUserIcon, imageViewBackButton;
    EditText editTextEmail, editTextName, editTextEducationLV, editTextCollege;
    TextView textViewSaveButton;
    //Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    String encodedUserIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_account);
        getSupportActionBar().hide();  //action bar hidden
        init();
        firebaseSetup();

    }

    private void init() {
        imageViewUserIcon = findViewById(R.id.fragment_my_account_icon);
        editTextEmail = findViewById(R.id.fragment_my_account_email_input);
        editTextName = findViewById(R.id.fragment_my_account_user_name_input);
        editTextEducationLV = findViewById(R.id.fragment_my_account_education_input);
        editTextCollege = findViewById(R.id.fragment_my_account_college_input);
        textViewSaveButton = findViewById(R.id.fragment_my_account_saveBTN);
        imageViewBackButton = findViewById(R.id.fragment_my_account_backBTN);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyAccountActivity.this, MainActivity.class));
                finish();
            }
        });

        textViewSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();
            }
        });

        imageViewUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserIcon();
            }
        });
    }

    private void firebaseSetup() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Users");
        userID = firebaseUser.getUid();

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userProfile = snapshot.getValue(UserModel.class);

                if (userProfile != null) {
                    editTextName.setText(userProfile.getUserName());
                    editTextEmail.setText(userProfile.getEmail());
                    editTextEducationLV.setText(userProfile.getEducation());
                    editTextCollege.setText(userProfile.getCollege());

                    //decode image
                    byte[] bytes = Base64.decode(userProfile.getEncodedUserIcon(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewUserIcon.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUserInformation() {
        String email = editTextEmail.getText().toString().trim();
        String userName = editTextName.getText().toString().trim();
        String educationLevel = editTextEducationLV.getText().toString().trim();
        String college = editTextCollege.getText().toString().trim();


        if (userName.isEmpty()) {
            editTextName.setError("User name is required!");
            editTextName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        UserModel userModel = new UserModel(userName, email, userID, educationLevel, college, encodedUserIcon);
        databaseReference.child(userID).setValue(userModel);
        Toast.makeText(MyAccountActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();

    }

    private void updateUserIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageViewUserIcon.setImageBitmap(bitmap);
                            encodedUserIcon = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}