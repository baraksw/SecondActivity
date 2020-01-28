package com.example.secondproject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import com.example.secondproject.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersMap {
    public HashMap<String, User> users_db;


    public UsersMap() {
        users_db = new HashMap<String, User>();
    }

    public HashMap<String, User> getUsers_db() {
        return users_db;
    }

    public void setUsers_db(HashMap<String, User> users_db) {
        this.users_db = users_db;
    }

    public void add_user(User user) {
        users_db.put(user.getFull_name(), user);
    }

    public void remove_user(User user) {
        users_db.remove(user.getFull_name());
    }

    public void clear() {
        users_db.clear();
    }

    public User getUser(String name)
    {
        return users_db.get(name);
    }

    public void add_friends(User user) {

        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("DB");
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User current_user = keyNode.getValue(User.class);
                    //user.add_friend(current_user.getFull_name());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
