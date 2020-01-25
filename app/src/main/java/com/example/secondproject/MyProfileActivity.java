package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MyProfileActivity extends AppCompatActivity {

    private RecyclerView hums_recyclerView;
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
}
