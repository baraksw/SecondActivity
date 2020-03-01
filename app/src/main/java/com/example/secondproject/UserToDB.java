package com.example.secondproject;

import androidx.annotation.NonNull;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public interface UserToDB {

    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    default void set_user(User user) {
        mDataBase.child("DB").child("users_db").child(user.getFull_name()).setValue(user);
    }

    default void update_XP_in_DB(User user) {
        mDataBase.child("db2").child("hums").child(user.getFull_name()).child("xp_cnt").setValue(user.getXp_cnt());
    }

    default int getXP(User user) {
        int xp = -1;
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int xp = dataSnapshot.child("DB").child("users_db").child(user.getFull_name()).child("xp_cnt").getValue(int.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return xp;
    }
}
