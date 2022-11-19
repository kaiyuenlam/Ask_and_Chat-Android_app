package com.example.askchat.fragment.chatfunc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    OnChatListClickListener onClickListener;
    List<ChatRoomModel> list;

    public ChatListAdapter(OnChatListClickListener onClickListener, List<ChatRoomModel> list) {
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item_view, parent, false);

        return new ViewHolder(itemView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoomModel chatRoomModel = list.get(position);
        FirebaseDatabase.getInstance().getReference("Users").child(chatRoomModel.getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        byte[] bytes = Base64.decode(userModel.getEncodedUserIcon(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageViewUserIcon.setImageBitmap(bitmap);
                        holder.textViewUserName.setText(userModel.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.textViewMessage.setText(chatRoomModel.getMessage());
        holder.textViewTime.setText(getBetterTime(chatRoomModel.getTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getBetterTime(String input) {
        LocalDateTime localDateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        if (localDateTime.toLocalDate().equals(LocalDate.now())) {
            return localDateTime.toLocalTime().toString();
        }
        return localDateTime.toString();
    }

    public interface OnChatListClickListener {
        void ToChatActivity(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageViewUserIcon;
        TextView textViewUserName, textViewMessage, textViewTime;
        OnChatListClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnChatListClickListener onClickListener) {
            super(itemView);
            imageViewUserIcon = itemView.findViewById(R.id.chat_list_user_image);
            textViewUserName = itemView.findViewById(R.id.chat_list_user_name);
            textViewMessage = itemView.findViewById(R.id.chat_list_last_message);
            textViewTime = itemView.findViewById(R.id.chat_list_time);
            this.onClickListener = onClickListener;

            imageViewUserIcon.setOnClickListener(this);
            textViewTime.setOnClickListener(this);
            textViewMessage.setOnClickListener(this);
            textViewUserName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.ToChatActivity(getAdapterPosition());
        }
    }
}
