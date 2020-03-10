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

}
