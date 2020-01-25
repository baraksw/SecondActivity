package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyProfileActivity extends AppCompatActivity {

    private RecyclerView hums_recyclerView;
    Hum hum1 = new Hum("Barak", "200120_1736.15ac69e1-8f0f-4deb-9790-e9292a2ee2f4", 1);
    Hum hum2 = new Hum("asaf", "200120_1736.e573133c-9c68-4311-893d-bd5eefc9fe97", 2);
    Hum hum3 = new Hum("tomer", "200120_1805.69397aea-7898-40c9-be80-199f10aa6c07", 3);
    Hum hum4 = new Hum("tuval", "dddd", 4);
    Hum hum5 = new Hum("atara", "eeee", 5);
    Hum hum6 = new Hum("yair", "ffff", 6);
    Hum hum7 = new Hum("jimmy", "gggg", 7);
    Hum hum8 = new Hum("alex", "hhhh", 8);
    Hum hum9 = new Hum("akazada", "iiii", 9);
    private Hum[] temp_hums = {hum1, hum2, hum3, hum4, hum5, hum6, hum7, hum8, hum9};
    private RecyclerView.LayoutManager hum_layoutManager;
    private HumRecyclerAdapter hum_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        hums_recyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        hum_layoutManager = new GridLayoutManager(this, 1);
        hums_recyclerView.setHasFixedSize(true);
        hums_recyclerView.setLayoutManager(hum_layoutManager);
        hum_adapter = new HumRecyclerAdapter(temp_hums);
        hums_recyclerView.setAdapter(hum_adapter);
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
