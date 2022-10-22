package com.example.askchat.fragment.homefunc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askchat.MainActivity;
import com.example.askchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class AddNewPostActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageViewBackButton, imageViewUploadImageButton, imageViewUploadedImage;
    TextView textViewShareButton, textViewViewImage;
    SwitchCompat switchCompatSetAnonymous;
    EditText editTextQuestion;

    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        getSupportActionBar().hide();  //action bar hidden
        bindView();

    }

    private void bindView() {
        imageViewBackButton = findViewById(R.id.addNewPost_back_button);
        textViewShareButton = findViewById(R.id.addNewPost_upload_button);
        switchCompatSetAnonymous = findViewById(R.id.addNewPost_switch_button);
        editTextQuestion = findViewById(R.id.addNewPost_questions);
        textViewViewImage = findViewById(R.id.addNewPost_viewImage);
        imageViewUploadImageButton = findViewById(R.id.addNewPost_image_button);
        imageViewUploadedImage = findViewById(R.id.addNewPost_image);

        imageViewBackButton.setOnClickListener(this);
        textViewShareButton.setOnClickListener(this);
        textViewViewImage.setOnClickListener(this);
        imageViewUploadImageButton.setOnClickListener(this);
        imageViewUploadedImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewPost_back_button:
                finish();
                break;

            case R.id.addNewPost_upload_button:
                shareQuestion();
                break;

            case R.id.addNewPost_viewImage:
                showUploadedImage();
                break;

            case R.id.addNewPost_image_button:
                uploadImage();
                break;

            case R.id.addNewPost_image:
                imageViewUploadedImage.setVisibility(View.GONE);
        }
    }

    private void shareQuestion() {
        String question = editTextQuestion.getText().toString().trim();
        String image = encodedImage;

        if (image == null) {
            image = "";
        }

        if (question.isEmpty()) {
            editTextQuestion.setError("Question is required!");
            editTextQuestion.requestFocus();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        String postID = databaseReference.push().getKey();

        PostModel postModel = new PostModel();
        postModel.setPostID(postID);
        postModel.setQuestion(question);
        postModel.setImage(image);
        postModel.setVoteCounter("0");

        if (switchCompatSetAnonymous.isChecked()) {
            postModel.setPublisher("2EAKYVJBRDPS8PSM0Mm2srOF1fF2");
        } else {
            postModel.setPublisher(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        databaseReference.child(postID).setValue(postModel);
        Toast.makeText(AddNewPostActivity.this, "Shared successfully!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(AddNewPostActivity.this, MainActivity.class));
        finish();
    }

    private void showUploadedImage() {
        imageViewUploadedImage.setVisibility(View.VISIBLE);
    }

    private void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
        textViewViewImage.setVisibility(View.VISIBLE);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 350;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
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
                            imageViewUploadedImage.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

}