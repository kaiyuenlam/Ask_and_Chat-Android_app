package com.example.askchat.fragment.homefunc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageViewBackButton, imageViewPostImage;
    TextView textViewTitle, textViewViewImageButton, textViewNoComment;
    RecyclerView recyclerView;
    EditText editTextInputComment;
    CardView cardViewSendButton;

    List<CommentModel> list;
    String postID;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().hide();  //action bar hidden
        postID = getIntent().getStringExtra("postID");
        init();
    }

    private void init() {
        imageViewBackButton = findViewById(R.id.post_activity_back_image_view);
        imageViewPostImage = findViewById(R.id.post_activity_image);
        textViewTitle = findViewById(R.id.post_activity_question_text_view);
        textViewViewImageButton = findViewById(R.id.post_activity_view_image_text_view);
        recyclerView = findViewById(R.id.post_activity_recycler_view);
        editTextInputComment = findViewById(R.id.post_activity_edit_text);
        cardViewSendButton = findViewById(R.id.post_activity_send_card_view);
        textViewNoComment = findViewById(R.id.post_activity_first_to_comment_text);

        imageViewBackButton.setOnClickListener(this);
        textViewViewImageButton.setOnClickListener(this);
        cardViewSendButton.setOnClickListener(this);
        imageViewPostImage.setOnClickListener(this);

        FirebaseDatabase.getInstance().getReference("Posts").child(postID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostModel postModel = snapshot.getValue(PostModel.class);
                        if (postModel.getImage().isEmpty()) {
                            imageViewPostImage.setVisibility(View.GONE);
                            textViewViewImageButton.setVisibility(View.GONE);
                        } else {
                            byte[] bytes = Base64.decode(postModel.getImage(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageViewPostImage.setImageBitmap(bitmap);
                            imageViewPostImage.setVisibility(View.VISIBLE);
                            textViewViewImageButton.setVisibility(View.GONE);
                        }

                        textViewTitle.setText(postModel.getQuestion());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        textViewTitle.setMovementMethod(new ScrollingMovementMethod());

        list = new ArrayList<>();
        adapter = new CommentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        readComment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_activity_back_image_view:
                finish();
                break;
            case R.id.post_activity_view_image_text_view:
                imageViewPostImage.setVisibility(View.VISIBLE);
                textViewViewImageButton.setVisibility(View.GONE);
                break;
            case R.id.post_activity_image:
                imageViewPostImage.setVisibility(View.GONE);
                textViewViewImageButton.setVisibility(View.VISIBLE);
                break;
            case R.id.post_activity_send_card_view:
                comment();
                break;
        }
    }

    private void comment() {
        if (editTextInputComment.getText().toString().isEmpty()) {
            editTextInputComment.setError("Comment can't be empty");
            editTextInputComment.requestFocus();
        } else {
            CommentModel commentModel = new CommentModel();
            commentModel.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
            commentModel.setComment(editTextInputComment.getText().toString().trim());
            commentModel.setTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm:ss", Locale.ENGLISH)).toString());
            String key = FirebaseDatabase.getInstance().getReference("Comments").push().getKey();
            FirebaseDatabase.getInstance().getReference("Comments").child(postID).child(key)
                    .setValue(commentModel);
        }
    }

    private void readComment() {
        FirebaseDatabase.getInstance().getReference("Comments").child(postID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            list.add(dataSnapshot.getValue(CommentModel.class));
                        }
                        adapter.notifyDataSetChanged();

                        if (list.size() > 0) {
                            textViewNoComment.setVisibility(View.GONE);
                        } else {
                            textViewNoComment.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}