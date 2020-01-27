package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secondproject.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendsPageActivity extends AppCompatActivity {

    private RecyclerView friend_recyclerView;
    private RecyclerView.LayoutManager friend_layoutManager;
    private FriendRecyclerAdapter friend_adapter;
    private ArrayList<User> my_friends_temp;
    private DatabaseReference db_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        friend_recyclerView = findViewById(R.id.my_friends_recyclerView);
        friend_layoutManager = new GridLayoutManager(this, 1);
        friend_recyclerView.setHasFixedSize(true);
        friend_recyclerView.setLayoutManager(friend_layoutManager);

        db_reference = FirebaseDatabase.getInstance().getReference().child("DB").child("users_db");
        my_friends_temp = new ArrayList<User>();
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    my_friends_temp.add(user);
                }
                friend_adapter = new FriendRecyclerAdapter(FriendsPageActivity.this, my_friends_temp);
                friend_recyclerView.setAdapter(friend_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsPageActivity.this, "Oops... something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void launchMyProfilePage(View view) {
        Intent myProfileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileIntent);
    }

    public void launchFriendProfileActivity(View view) {
        Intent myFriendProfileIntent = new Intent(this, FriendProfileActivity.class);
        startActivity(myFriendProfileIntent);
    }
}
