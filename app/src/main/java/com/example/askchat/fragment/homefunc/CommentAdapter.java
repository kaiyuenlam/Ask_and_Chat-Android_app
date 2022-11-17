package com.example.askchat.fragment.homefunc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<CommentModel> list;

    public CommentAdapter(List<CommentModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel commentModel = list.get(position);
        FirebaseDatabase.getInstance().getReference("Users").child(commentModel.getUserID())
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
        holder.textViewComment.setText(commentModel.getComment());
        holder.textViewTime.setText(commentModel.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName, textViewTime, textViewComment;
        ImageView imageViewUserIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewComment = itemView.findViewById(R.id.comment_item_comment);
            textViewTime = itemView.findViewById(R.id.comment_item_time);
            textViewUserName = itemView.findViewById(R.id.comment_item_user_name);
            imageViewUserIcon = itemView.findViewById(R.id.comment_item_user_icon);
        }
    }
}
