package com.example.askchat.fragment.chatfunc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_contact_list_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUserIcon;
        TextView textViewUserName, textViewUserEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserIcon = itemView.findViewById(R.id.contact_list_icon);
            textViewUserEmail = itemView.findViewById(R.id.contact_list_email);
            textViewUserName = itemView.findViewById(R.id.contact_list_user_name);

        }
    }
}
