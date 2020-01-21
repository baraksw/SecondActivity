package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class HomePage extends AppCompatActivity {

    //TODO: Create a list of ImageButton.
    ImageButton hum1,hum2;
    //TODO: Create a list of EditText.
    EditText answer1EditText, answer2EditText;
    ScrollView storyScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        answer1EditText = (EditText) findViewById(R.id.answer1_editText);
        answer1EditText.setVisibility(View.GONE);
        storyScrollView.setVisibility(View.GONE);
    }

    public void launchPianoActivity(View view) {
        Intent intent = new Intent(this, PianoActivity.class);
        startActivity(intent);
    }

    public void openStory(View view) {
        storyScrollView.setVisibility(View.VISIBLE);
    }

    public void hum_1_clicked(View view) {
        answer1EditText.setVisibility(View.VISIBLE);
    }
    public void hum_2_clicked(View view) {
        answer2EditText.setVisibility(View.VISIBLE);
    }
}
