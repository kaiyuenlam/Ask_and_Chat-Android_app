package com.example.askchat.fragment.homefunc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askchat.R;
import com.example.askchat.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public List<PostModel> listPosts;
    OnPostClickListener onPostClickListener;
    OnPostLongClickListener onPostLongClickListener;
    List<String> favorList;

    public PostAdapter(List<PostModel> listPosts, OnPostClickListener onPostClickListener) {
        this.listPosts = listPosts;
        this.onPostClickListener = onPostClickListener;
    }

    public PostAdapter(List<PostModel> listPosts, OnPostClickListener onPostClickListener
            , OnPostLongClickListener onPostLongClickListener, List<String> favorList) {
        this.listPosts = listPosts;
        this.onPostClickListener = onPostClickListener;
        this.onPostLongClickListener = onPostLongClickListener;
        this.favorList = favorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postlist_items, parent, false);
        if (onPostLongClickListener != null) {
            return new ViewHolder(itemView, onPostClickListener, onPostLongClickListener);
        }
        return new ViewHolder(itemView, onPostClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostModel postModel = listPosts.get(position);

        if (postModel.getImage().isEmpty()) {
            holder.imageViewQuestion.setVisibility(View.GONE);
        } else {
            byte[] bytes = Base64.decode(postModel.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imageViewQuestion.setImageBitmap(bitmap);
            holder.imageViewQuestion.setVisibility(View.VISIBLE);
        }

        holder.textViewQuestion.setText(postModel.getQuestion());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(postModel.getPublisher())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        holder.textViewUserName.setText(userModel.getUserName());

                        byte[] bytes = Base64.decode(userModel.getEncodedUserIcon(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageViewIcon.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if (onPostLongClickListener != null) {
            if (favorList.contains(postModel.getPostID())) {
                holder.imageViewStar.setVisibility(View.VISIBLE);
            }
        }
        //setVoteText(holder, postModel);
    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    private void setVoteText(ViewHolder viewHolder, PostModel postModel) {
        BigDecimal bigDecimal = new BigDecimal(postModel.getVoteCounter());
        int a = bigDecimal.compareTo(BigDecimal.ZERO);

        if (a < 0) {
            viewHolder.imageViewDownVote.setVisibility(View.VISIBLE);
            viewHolder.imageViewUpvote.setVisibility(View.INVISIBLE);
        } else {
            // vote > 0
            viewHolder.imageViewDownVote.setVisibility(View.INVISIBLE);
            viewHolder.imageViewUpvote.setVisibility(View.VISIBLE);
        }
        viewHolder.textViewVoteCounter.setText(bigDecimal.abs().toString());
    }

    public interface OnPostClickListener {
        void OnPostClick(int position);
    }

    public interface OnPostLongClickListener {
        void OnPostLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        CardView cardViewPost;
        ImageView imageViewIcon, imageViewQuestion, imageViewUpvote, imageViewDownVote, imageViewStar;
        TextView textViewUserName, textViewQuestion, textViewVoteCounter;
        OnPostClickListener onPostClickListener;
        OnPostLongClickListener onPostLongClickListener;

        public ViewHolder(@NonNull View itemView, OnPostClickListener onPostClickListener) {
            super(itemView);
            bindView(itemView);
            this.onPostClickListener = onPostClickListener;
            itemView.setOnClickListener(this);
            imageViewUpvote.setVisibility(View.INVISIBLE);
            imageViewDownVote.setVisibility(View.INVISIBLE);
            textViewVoteCounter.setVisibility(View.INVISIBLE);
        }

        public ViewHolder(@NonNull View itemView, OnPostClickListener onPostClickListener, OnPostLongClickListener onPostLongClickListener) {
            super(itemView);
            bindView(itemView);
            this.onPostClickListener = onPostClickListener;
            this.onPostLongClickListener = onPostLongClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageViewUpvote.setVisibility(View.INVISIBLE);
            imageViewDownVote.setVisibility(View.INVISIBLE);
            textViewVoteCounter.setVisibility(View.INVISIBLE);
        }

        private void bindView(View itemView) {
            cardViewPost = itemView.findViewById(R.id.postList_clickable_post);
            imageViewIcon = itemView.findViewById(R.id.comment_user_icon);
            imageViewQuestion = itemView.findViewById(R.id.comment_question_image);
            imageViewUpvote = itemView.findViewById(R.id.comment_upvote_image);
            imageViewDownVote = itemView.findViewById(R.id.postList_downvote_image);
            textViewUserName = itemView.findViewById(R.id.comment_user_name);
            textViewQuestion = itemView.findViewById(R.id.input_comment_text);
            textViewVoteCounter = itemView.findViewById(R.id.postList_vote_count);
            imageViewStar = itemView.findViewById(R.id.postList_star);
        }

        @Override
        public void onClick(View view) {
            onPostClickListener.OnPostClick(getAdapterPosition());
            Log.e("post adapter", "on click listener");
        }

        @Override
        public boolean onLongClick(View view) {
            onPostLongClickListener.OnPostLongClick(getAdapterPosition());
            if (imageViewStar.getVisibility() == View.VISIBLE) {
                imageViewStar.setVisibility(View.INVISIBLE);
            } else {
                imageViewStar.setVisibility(View.VISIBLE);
            }
            return false;
        }
    }

}
