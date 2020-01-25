package com.example.secondproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder> {
    private User[] my_friends;

    public FriendRecyclerAdapter(User[] my_friends){
        this.my_friends = my_friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_layout, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);

        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

        holder.friendName.setText(my_friends[position].getFull_name());
        holder.xpTextView.setText(my_friends[position].getXp_cnt() + " XP");

    }

    @Override
    public int getItemCount() {
        return my_friends.length;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView xpTextView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_row_full_name);
            xpTextView = itemView.findViewById(R.id.friend_row_xp_textView);
        }
    }
}
