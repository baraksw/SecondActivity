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
    private RecyclerView humsRecyclerView;
    private ArrayList<Hum> tempHums;
    private RecyclerView.LayoutManager humLayoutManager;
    private HumRecyclerAdapter humAdapter;
    private DatabaseReference dbHumsReference;
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;
    private String currentUser;
    private TextView XPTextView;
    private TextView profileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "My profile's onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = String.valueOf(mAuth.getCurrentUser().getDisplayName());
        profileName = findViewById(R.id.friend_name_textView);
        profileName.setText(currentUser);

        XPTextView = findViewById(R.id.xp_points_textView);
        showXPText();

        humLayoutManager = new GridLayoutManager(this, 1);
        humsRecyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        humsRecyclerView.setHasFixedSize(true);
        humsRecyclerView.setLayoutManager(humLayoutManager);

        tempHums = new ArrayList<Hum>();

        dbHumsReference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        dbHumsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempHums.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    if(hum.owner.equals(currentUser) == true){
                        hum.hum_answer = dataSnapshot1.child("hum_answer").getValue(String.class);
                        tempHums.add(hum);
                    }
                }
                humAdapter = new HumRecyclerAdapter(MyProfileActivity.this, tempHums);
                humsRecyclerView.setAdapter(humAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showXPText() {
        if(mAuth.getCurrentUser()!=null) {
            dbReference = FirebaseDatabase.getInstance().getReference();
            dbReference.child("DB").child("users_db").child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    XPTextView.setText("XP: "+ String.valueOf(dataSnapshot.child("xp_cnt").getValue(Integer.class)));
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
