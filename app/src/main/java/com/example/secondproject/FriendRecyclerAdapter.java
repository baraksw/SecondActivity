package com.example.secondproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder> {
    private ArrayList<User> myFriends;

    public FriendRecyclerAdapter( ArrayList<User> myFriends){
        this.myFriends = myFriends;
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
        holder.friendName.setText(myFriends.get(position).getFull_name());
        //holder.xpTextView.setText(myFriends.get(position).getXp_cnt() + " XP");
        setFriendXPText(holder, myFriends.get(position));
        //TODO - get the actual xp
    }

    private void setFriendXPText(FriendViewHolder holder, User user) {
        DatabaseReference mProfileDb = FirebaseDatabase.getInstance().getReference();
        mProfileDb.child("DB").child("users_db").child(user.getFull_name()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.xpTextView.setText(String.valueOf(dataSnapshot.child("xp_cnt").getValue(Integer.class)) + " XP");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Set friend xp", "setting friend's xp canceled");
            }
        });
    }

    @Override
    public int getItemCount() {
        return myFriends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView xpTextView;
        private String current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_row_full_name);
            xpTextView = itemView.findViewById(R.id.friend_row_xp_textView);
        }
    }

}
