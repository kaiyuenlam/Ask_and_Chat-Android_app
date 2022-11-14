package com.example.askchat.fragment.chatfunc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    OnClickListener onClickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item_view, parent, false);

        return new ViewHolder(itemView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnClickListener{
        void ToChatActivity(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageViewUserIcon;
        TextView textViewUserName, textViewMessage, textViewTime;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            imageViewUserIcon = itemView.findViewById(R.id.chat_list_user_image);
            textViewUserName = itemView.findViewById(R.id.chat_list_user_name);
            textViewMessage = itemView.findViewById(R.id.chat_list_last_message);
            textViewTime = itemView.findViewById(R.id.chat_list_time);

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
