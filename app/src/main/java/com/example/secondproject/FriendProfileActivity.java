package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class FriendProfileActivity extends AppCompatActivity {

    private TextView profile_name;
    private String current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    //private TextView profile_xp;
    //private String current_xp = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getXp());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        //profile_xp = findViewById(R.id.xp_points_textView);
        //profile_xp.setText(current_xp);
        profile_name = findViewById(R.id.user_name_textView);
        profile_name.setText(current_user);
    }

    public void launchFriendsPageActivity(View view) {
        Intent friendsPageIntent = new Intent(this, FriendsPageActivity.class);
        startActivity(friendsPageIntent);
    }

    public void launchFriendShazamzamsActivity(View view) {
        Intent friendsShazamzamsIntent = new Intent(this, FriendShazamzamsActivity.class);
        startActivity(friendsShazamzamsIntent);
    }
}
