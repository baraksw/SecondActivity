package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    Hum hum1 = new Hum("Barak", "200120_1736.15ac69e1-8f0f-4deb-9790-e9292a2ee2f4", 1);
    Hum hum2 = new Hum("asaf", "200120_1736.e573133c-9c68-4311-893d-bd5eefc9fe97", 2);
    Hum hum3 = new Hum("tomer", "200120_1805.69397aea-7898-40c9-be80-199f10aa6c07", 3);
    Hum hum4 = new Hum("tuval", "dddd", 4);
    Hum hum5 = new Hum("atara", "eeee", 5);
    Hum hum6 = new Hum("yair", "ffff", 6);
    Hum hum7 = new Hum("jimmy", "gggg", 7);
    Hum hum8 = new Hum("alex", "hhhh", 8);
    Hum hum9 = new Hum("akazada", "iiii", 9);

    private RecyclerView hums_recyclerView;
    private ArrayList<Hum> temp_hums;
    private RecyclerView.LayoutManager hum_layoutManager;
    private HumRecyclerAdapter hum_adapter;
    private DatabaseReference db_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        hums_recyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        hum_layoutManager = new GridLayoutManager(this, 1);
        hums_recyclerView.setHasFixedSize(true);
        hums_recyclerView.setLayoutManager(hum_layoutManager);

        db_reference = FirebaseDatabase.getInstance().getReference().child("hums_db");
        temp_hums = new ArrayList<Hum>();
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    temp_hums.add(hum);
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
