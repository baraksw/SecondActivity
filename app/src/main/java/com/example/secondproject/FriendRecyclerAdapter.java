package com.example.secondproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexzh.circleimageview.CircleImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder> {
    private ArrayList<User> my_friends;
    Context context;
    //private TextView profile_xp;
    //private String current_xp = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getXp());

    public FriendRecyclerAdapter(Context context, ArrayList<User> my_friends){
        this.my_friends = my_friends;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_layout, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        //profile_xp = findViewById(R.id.friend_row_xp_textView);
        //profile_xp.setText(current_xp);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.friendName.setText(my_friends.get(position).getFull_name());
        holder.xpTextView.setText(my_friends.get(position).getXp_cnt() + " XP");

        holder.circleImage.setOnClickListener((view) -> {
            Intent friendProfileIntent = new Intent(context, FriendProfileActivity.class);
            context.startActivity(friendProfileIntent);
        });
    }

    @Override
    public int getItemCount() {
        return my_friends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView xpTextView;
        com.alexzh.circleimageview.CircleImageView circleImage;
        private TextView profile_name;
        private String current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_row_full_name);
            xpTextView = itemView.findViewById(R.id.friend_row_xp_textView);
            circleImage = itemView.findViewById(R.id.friend_profile_picture);

            //profile_name = itemView.findViewById(R.id.user_name_textView);
            //profile_name.setText(current_user);
        }
    }

}
