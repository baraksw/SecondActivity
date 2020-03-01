package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.google.firebase.database.core.Context;

import java.util.ArrayList;


public class MyProfileActivity extends AppCompatActivity {

    final static String LOG_TAG = "My profile log";
    private RecyclerView hums_recyclerView;
    private ArrayList<Hum> temp_hums;
    private RecyclerView.LayoutManager hum_layoutManager;
    private HumRecyclerAdapter hum_adapter;
    private DatabaseReference db_reference;
    private DatabaseReference mProfileDb;
    private DatabaseReference hums_ref;
    private FirebaseAuth mAuth;
    private ArrayList<String> my_hums;
    private String current_user;

    private TextView profile_name;
    //private TextView profile_xp;
    //private String current_xp = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getXp());
    // TODO: complete the function "getXp"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "My profile's onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        mProfileDb = FirebaseDatabase.getInstance().getReference();
        hums_ref = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");

        hums_recyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        hum_layoutManager = new GridLayoutManager(this, 1);
        hums_recyclerView.setHasFixedSize(true);
        hums_recyclerView.setLayoutManager(hum_layoutManager);

        /*
        my_hums = new ArrayList<String>();
        mProfileDb.child("Hums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    my_hums.add(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //profile_xp = findViewById(R.id.xp_points_textView);
        //profile_xp.setText(current_xp);
        profile_name = findViewById(R.id.user_name_textView);
        profile_name.setText(current_user);

        db_reference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        temp_hums = new ArrayList<Hum>();
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    if(hum.owner.equals(current_user)==true){
                        temp_hums.add(hum);
                    }
                }
                hum_adapter = new HumRecyclerAdapter(MyProfileActivity.this, temp_hums);
                hums_recyclerView.setAdapter(hum_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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
