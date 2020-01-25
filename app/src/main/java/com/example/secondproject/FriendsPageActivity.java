package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secondproject.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FriendsPageActivity extends AppCompatActivity {

    private RecyclerView friend_recyclerView;
    private RecyclerView.LayoutManager friend_layoutManager;
    private FriendRecyclerAdapter friend_adapter;

    private User user1 = new User();
    private User user2 = new User();
    private User user3 = new User();
    private User user4 = new User();
    private User user5 = new User();
    private User user6 = new User();
    private User user7 = new User();
    private User user8 = new User();
    private User user9 = new User();
    private User user10 = new User();
    private User user11 = new User();
    private User user12 = new User();
    private User user13 = new User();
    private User my_friends_temp[] = {user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);
        friend_recyclerView = findViewById(R.id.my_friends_recyclerView);
        friend_layoutManager = new GridLayoutManager(this, 1);
        friend_recyclerView.setHasFixedSize(true);
        friend_recyclerView.setLayoutManager(friend_layoutManager);
        friend_adapter = new FriendRecyclerAdapter(my_friends_temp);
        friend_recyclerView.setAdapter(friend_adapter);

    }

    public void launchMyProfilePage(View view) {
        Intent myProfileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileIntent);
    }
}
