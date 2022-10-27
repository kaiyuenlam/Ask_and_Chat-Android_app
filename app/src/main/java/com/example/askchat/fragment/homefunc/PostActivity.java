package com.example.askchat.fragment.homefunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.askchat.R;
import com.example.askchat.fragment.HomeFragment;

import java.util.List;

public class PostActivity extends AppCompatActivity {

    EditText comment_text;
    ImageButton send_imgbtn,comment_backward_btn;
    RecyclerView comment_recyclerView;

    List<CommentModel> commentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        comment_text = findViewById(R.id.comment_text);
        send_imgbtn = findViewById(R.id.send_imgbtn);
        comment_backward_btn = findViewById(R.id.comment_backward_btn);
        comment_recyclerView = findViewById(R.id.comment_recyclerView);

        comment_backward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PostActivity.this, HomeFragment.class);
                //startActivity(intent);
                finish();
            }
        });

        send_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = comment_text.getText().toString();

                if (s.isEmpty()) {
                    comment_text.setError("Comment is required!");
                    comment_text.requestFocus();
                    return;
                }
            }
        });
    }
}