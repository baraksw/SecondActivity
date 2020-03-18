package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FriendProfileActivity extends AppCompatActivity {

    private TextView profile_name;
    private String friend_name;
    private TextView profile_xp;
    private int friend_xp;

    //private String current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    //private String current_xp = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getXp());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent current_friend = getIntent();
        friend_name = current_friend.getStringExtra("friend_name");
        friend_xp = current_friend.getIntExtra("friend_xp", -1);

        profile_name = findViewById(R.id.friend_profile_name_textView);
        profile_name.setText(friend_name);
        profile_xp = findViewById(R.id.friend_profile_xp_points_textView);
        profile_xp.setText(friend_xp + " Xp");
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
