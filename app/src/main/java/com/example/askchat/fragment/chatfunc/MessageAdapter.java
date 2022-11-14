package com.example.askchat.fragment.chatfunc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<MessageModel> list;

    public MessageAdapter(List<MessageModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel messageModel = list.get(position);
        String time = messageModel.getDate();
        if (messageModel.isSender()) {
            holder.receiveTime.setVisibility(View.GONE);
            holder.textViewReceive.setVisibility(View.GONE);
            holder.frameLayoutReceive.setVisibility(View.GONE);
            holder.sendTime.setText(time);
            holder.textViewSend.setText(messageModel.getMessage());
        } else {
            holder.sendTime.setVisibility(View.GONE);
            holder.textViewSend.setVisibility(View.GONE);
            holder.frameLayoutSend.setVisibility(View.GONE);
            holder.receiveTime.setText(time);
            holder.textViewReceive.setText(messageModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayoutSend, frameLayoutReceive;
        TextView textViewSend, textViewReceive, sendTime, receiveTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View view) {
            frameLayoutReceive = view.findViewById(R.id.message_receive);
            frameLayoutSend = view.findViewById(R.id.message_send);
            textViewReceive = view.findViewById(R.id.message_receive_textview);
            textViewSend = view.findViewById(R.id.message_send_textview);
            sendTime = view.findViewById(R.id.message_send_time);
            receiveTime = view.findViewById(R.id.message_receive_time);
        }
    }
}
