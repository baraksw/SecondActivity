package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FriendProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
    }

    public void launchFriendsPageActivity(View view) {
        Intent intent = new Intent(this, FriendsPageActivity.class);
        startActivity(intent);
    }

    public void launchFriendShazamzamsActivity(View view) {
        Intent intent = new Intent(this, FriendShazamzamsActivity.class);
        startActivity(intent);
    }
}
