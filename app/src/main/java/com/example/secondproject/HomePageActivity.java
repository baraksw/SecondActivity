package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView story_recyclerView;
    private ArrayList<Hum> story_hums;
    private RecyclerView.LayoutManager story_layoutManager;
    private StoryRecyclerAdapter story_adapter;
    private DatabaseReference db_reference;
    private boolean storyVisible = false;
    private FirebaseAuth mAuth;

    Hum temp_hum = new Hum("tuval", "200120_1736.15ac69e1-8f0f-4deb-9790-e9292a2ee2f4", 12);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        story_recyclerView = findViewById(R.id.stroy_recycleView);
        story_layoutManager = new GridLayoutManager(this, 1);
        story_recyclerView.setHasFixedSize(true);
        story_recyclerView.setLayoutManager(story_layoutManager);
        mAuth = FirebaseAuth.getInstance();


        story_recyclerView.setVisibility(View.GONE);
        db_reference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        story_hums = new ArrayList<Hum>();
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    story_hums.add(hum);
                    story_hums.add(temp_hum);
                }
                story_adapter = new StoryRecyclerAdapter(HomePageActivity.this, story_hums);
                story_recyclerView.setAdapter(story_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePageActivity.this, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openStory(View view) {
        if(!storyVisible){
            story_recyclerView.setVisibility(View.VISIBLE);
            storyVisible = true;
        }else if (storyVisible){
            story_recyclerView.setVisibility(View.GONE);
            storyVisible = false;
        }
    }

    public void launchMyProfilePage(View view) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    public void LogOut(View view){
        mAuth.signOut();
        Intent HomePageIntent = new Intent(HomePageActivity.this, HomePageDemo.class);
        startActivity(HomePageIntent);
    }
}
