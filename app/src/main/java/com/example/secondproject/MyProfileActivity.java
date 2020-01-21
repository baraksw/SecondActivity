package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MyProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Hum hum1 = new Hum("Barak", "aaaa", 1);
    Hum hum2 = new Hum("asaf", "bbbb", 2);
    Hum hum3 = new Hum("tomer", "cccc", 3);
    Hum hum4 = new Hum("tuval", "dddd", 4);
    Hum hum5 = new Hum("atara", "eeee", 5);
    Hum hum6 = new Hum("yair", "ffff", 6);
    Hum hum7 = new Hum("jimmy", "gggg", 7);
    Hum hum8 = new Hum("alex", "hhhh", 8);
    Hum hum9 = new Hum("akazada", "iiii", 9);
    private Hum[] temp_hums = {hum1, hum2, hum3, hum4, hum5, hum6, hum7, hum8, hum9};
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        recyclerView = findViewById(R.id.my_shazamzams_recyclerView);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(temp_hums);
        recyclerView.setAdapter(adapter);
    }
}
