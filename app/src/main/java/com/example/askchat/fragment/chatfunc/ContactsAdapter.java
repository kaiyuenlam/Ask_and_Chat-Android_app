package com.example.askchat.fragment.chatfunc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    List<String> list;
    AcceptedAdapter adapter;

    public ContactsAdapter(List<String> list) {
        this.list = list;
    }

    public ContactsAdapter(List<String> list, AcceptedAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_contact_list_items, parent, false);

        if (adapter != null) {
            return new ViewHolder(itemView, adapter);
        } else {
            return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AcceptedAdapter{
        void accepted(boolean isFriends, String userId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageViewUserIcon;
        TextView textViewUserName, textViewUserEmail, textViewAccept, textViewAccepted;
        CardView cardViewFriendButton;
        AcceptedAdapter acceptedAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindView();
        }

        public ViewHolder(@NonNull View itemView, AcceptedAdapter acceptedAdapter) {
            super(itemView);
            this.acceptedAdapter = acceptedAdapter;
            bindView();
        }

        private void bindView() {
            imageViewUserIcon = itemView.findViewById(R.id.contact_list_icon);
            textViewUserEmail = itemView.findViewById(R.id.contact_list_email);
            textViewUserName = itemView.findViewById(R.id.contact_list_user_name);
            cardViewFriendButton = itemView.findViewById(R.id.contact_list_accept_friend_button);
            textViewAccept = itemView.findViewById(R.id.contact_list_accept_friend_button_accept);
            textViewAccepted = itemView.findViewById(R.id.contact_list_accept_friend_button_accepted);

            if (acceptedAdapter == null) {
                cardViewFriendButton.setVisibility(View.GONE);
                textViewAccepted.setVisibility(View.GONE);
                textViewAccept.setVisibility(View.GONE);
            } else {
                cardViewFriendButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (textViewAccept.getVisibility() == View.VISIBLE) {
                textViewAccept.setVisibility(View.INVISIBLE);
                textViewAccepted.setVisibility(View.VISIBLE);
            } else if (textViewAccepted.getVisibility() == View.VISIBLE){
                textViewAccept.setVisibility(View.VISIBLE);
                textViewAccepted.setVisibility(View.INVISIBLE);
            }
        }
    }
}
