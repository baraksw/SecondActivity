package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    final static String LOG_TAG = "My profile log";
    private RecyclerView hums_RecyclerView;
    private ArrayList<Hum> temp_hums;
    private RecyclerView.LayoutManager hum_LayoutManager;
    private HumRecyclerAdapter hum_Adapter;
    private DatabaseReference hums_db_Reference;
    private DatabaseReference db_Reference;
    private FirebaseAuth mAuth;
    private String current_username;
    private TextView XP_TextView;
    private TextView profile_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Log.i(LOG_TAG, "My profile's onCreate");
        mAuth = FirebaseAuth.getInstance();
        current_username = String.valueOf(mAuth.getCurrentUser().getDisplayName());
        profile_name = findViewById(R.id.my_name_textView);
        profile_name.setText(current_username);

        XP_TextView = findViewById(R.id.xp_points_textView);
        showXPText();

        hum_LayoutManager = new GridLayoutManager(this, 1);
        hums_RecyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        hums_RecyclerView.setHasFixedSize(true);
        hums_RecyclerView.setLayoutManager(hum_LayoutManager);

        temp_hums = new ArrayList<Hum>();

        hums_db_Reference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        hums_db_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp_hums.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    if(hum.owner.equals(current_username) == true){ //Selecting only the hums of the current user
                        hum.hum_answer = dataSnapshot1.child("hum_answer").getValue(String.class);
                        temp_hums.add(hum);
                    }
                }
                hum_Adapter = new HumRecyclerAdapter(MyProfileActivity.this, temp_hums);
                hums_RecyclerView.setAdapter(hum_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showXPText() {
        if(mAuth.getCurrentUser()!=null) {
            db_Reference = FirebaseDatabase.getInstance().getReference();
            db_Reference.child("DB").child("users_db").child(current_username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    XP_TextView.setText("XP: "+ String.valueOf(dataSnapshot.child("xp_cnt").getValue(Integer.class)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void launchHomePage(View view) {
        Intent homePageIntent = new Intent(this, HomePageActivity.class);
        startActivity(homePageIntent);
    }

    public void launchMyFriendsPage(View view) {
        Intent myFriendsIntent = new Intent(this, FriendsPageActivity.class);
        startActivity(myFriendsIntent);
    }
}
