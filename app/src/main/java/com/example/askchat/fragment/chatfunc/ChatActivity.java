package com.example.askchat.fragment.chatfunc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageViewBackToChatButton, imageViewUserIcon;
    TextView textViewUserName;
    CardView cardViewSendButton;
    EditText editTextInputMessage;
    RecyclerView recyclerView;
    String friendsID;
    List<MessageModel> list;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();  //action bar hidden
        friendsID = getIntent().getStringExtra("friendID");
        bindView();
        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(list);
        recyclerView.setAdapter(messageAdapter);
        readMessage();
    }

    private void bindView() {
        imageViewBackToChatButton = findViewById(R.id.chat_activity_back_image_view);
        cardViewSendButton = findViewById(R.id.chat_activity_send_card_view);
        editTextInputMessage = findViewById(R.id.chat_activity_edit_text);
        recyclerView = findViewById(R.id.chat_activity_recycler_view);
        imageViewUserIcon = findViewById(R.id.chat_activity_user_icon);
        textViewUserName = findViewById(R.id.chat_activity_user_name);

        imageViewBackToChatButton.setOnClickListener(this);
        cardViewSendButton.setOnClickListener(this);

        FirebaseDatabase.getInstance().getReference("Users").child(friendsID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);

                byte[] bytes = Base64.decode(userModel.getEncodedUserIcon(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageViewUserIcon.setImageBitmap(bitmap);
                textViewUserName.setText(userModel.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chat_activity_back_image_view:
                finish();
                break;

            case R.id.chat_activity_send_card_view:
                sendMessage();
                editTextInputMessage.getText().clear();
                break;
        }
    }

    private void sendMessage() {
        if (editTextInputMessage.getText().toString().trim().isEmpty()) {
            //empty message
        } else {
            String messageID = FirebaseDatabase.getInstance().getReference("Message").push().getKey();

            MessageModel messageModel = new MessageModel();
            messageModel.setMessage(editTextInputMessage.getText().toString().trim());
            messageModel.setSender(true);
            messageModel.setLocalDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            FirebaseDatabase.getInstance().getReference("Message").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(friendsID).child(messageID).setValue(messageModel);

            messageModel.setSender(false);
            FirebaseDatabase.getInstance().getReference("Message").child(friendsID)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(messageID).setValue(messageModel);
        }
    }

    private void readMessage() {
        FirebaseDatabase.getInstance().getReference("Message")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(friendsID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                            list.add(messageModel);
                        }
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(list.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}