package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendsPageActivity extends AppCompatActivity {

    private FirebaseUser current;
    private int friends_number;
    private User current_user;
    private String current_user_name;
    private FirebaseAuth mAuth;
    private RecyclerView friend_recyclerView;
    private RecyclerView.LayoutManager friend_layoutManager;
    private FriendRecyclerAdapter friend_adapter;
    private ArrayList<User> users_list;
    private ArrayList<String> friends_list;
    private DatabaseReference users_db_reference;
    private DatabaseReference friends_db_reference;
    private String friend_name;
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private EditText mFriendField;

    final static String LOG_TAG = "Friends page log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "friends page's onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);
        mAuth=FirebaseAuth.getInstance();
        mFriendField = findViewById(R.id.adding_friends_editText);
        friend_recyclerView = findViewById(R.id.my_friends_recyclerView);
        friend_layoutManager = new GridLayoutManager(this, 1);
        friend_recyclerView.setHasFixedSize(true);
        friend_recyclerView.setLayoutManager(friend_layoutManager);
        current_user_name = String.valueOf(mAuth.getCurrentUser().getDisplayName());

        users_db_reference = FirebaseDatabase.getInstance().getReference().child("DB").child("users_db");
        friends_db_reference = FirebaseDatabase.getInstance().getReference().child("DB").child("users_db").child(current_user_name).child("friends");
        users_list = new ArrayList<User>();
        friends_list = new ArrayList<String>();

        friends_db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends_list.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String temp_friend = dataSnapshot1.getValue(String.class);
                    friends_list.add(temp_friend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsPageActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });

        users_db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users_list.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String temp_full_name = String.valueOf(dataSnapshot1.child("full_name").getValue(String.class));
                    String temp_user_name = String.valueOf(dataSnapshot1.child("user_name").getValue(String.class));
                    int temp_xp = dataSnapshot1.child("xp_cnt").getValue(Integer.class);

                    for (int i = 0; i < friends_list.size(); i++) {
                        if (friends_list.get(i).equals(temp_full_name)){
                            User local_user = new User(temp_full_name, temp_user_name, temp_xp);
                            users_list.add(local_user);

                        }
                    }
                }

                friend_adapter = new FriendRecyclerAdapter(users_list);
                friend_recyclerView.setAdapter(friend_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsPageActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void launchMyProfilePage(View view) {
        Intent myProfileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileIntent);
    }

    public void addFriendBtn(View view) {
        friend_name = mFriendField.getText().toString().trim();
        mDataBase.child("DB").child("users_db").child(mAuth.getCurrentUser().getDisplayName().toString()).child("friends").child(friend_name).setValue(friend_name);

    }

}
