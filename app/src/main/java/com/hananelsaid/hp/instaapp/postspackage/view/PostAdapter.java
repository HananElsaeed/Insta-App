package com.hananelsaid.hp.instaapp.postspackage.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.ShowPostImage;
import com.hananelsaid.hp.instaapp.postspackage.model.Post;
import com.hananelsaid.hp.instaapp.profilepackage.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private Context context;
    private List<Post> postlist;
    private List<User> userlist;


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_raw, parent, false);
        PostHolder PostHolder = new PostHolder(view);
        return PostHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        final Post post = postlist.get(position);
        final User user = userlist.get(0);
        holder.tvTitle.setText(post.getTitle());
        holder.tvDescription.setText(post.getDescription());
        holder.tvUserName.setText(user.getName());
        holder.ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowPostImage.class);
                intent.putExtra("postimageUrl", post.getImageUrl());
                context.startActivity(intent);
            }
        });
        holder.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowPostImage.class);
                intent.putExtra("profileimageUrl", user.getProfileImageUrl());
                context.startActivity(intent);
            }
        });

        holder.ivDeletPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(position);
            }
        });


       /*Glide.with(context)
                .load(post.getImageUrl())
                .into( holder.ivPost);*/
        Log.i("TAG", "onBindViewHolder: "+user.getProfileImageUrl());
        Log.i("TAG", "onBindViewHolder: "+post.getImageUrl());
        Picasso.get().load(post.getImageUrl()).into(holder.ivPost);
        Picasso.get().load(user.getProfileImageUrl()).into(holder.ivUserProfile);

    }

    public void deletePost(int position) {

        Post post = postlist.get(position);
        String postId = post.getPostId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("PostId").equalTo(postId);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public int getItemCount() {
        //postlist != null ? postlist.size() : 0;
        if (postlist != null && userlist != null)
            return postlist.size();
        else return 0;
    }

    public void setData(List<Post> postlist) {
        this.postlist = postlist;
        notifyDataSetChanged();

    }

    public void setUserData(List<User> userlist) {
        this.userlist = userlist;
        notifyDataSetChanged();

    }

    class PostHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvUserName;
        ImageView ivPost, ivUserProfile, ivDeletPost;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivPost = itemView.findViewById(R.id.ivpost);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            ivDeletPost = itemView.findViewById(R.id.ivDeletPost);


        }
    }
}
