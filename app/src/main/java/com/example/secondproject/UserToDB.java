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


    default User getUser(String name){
        User user = new User();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersMap usersMap = dataSnapshot.child("DB").getValue(UsersMap.class);
                User user = usersMap.getUser(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return user;

    }

}
