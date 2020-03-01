package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;
import com.jgabrielfreitas.core.BlurImageView;

import java.io.IOException;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView story_recyclerView;
    private ArrayList<Hum> story_hums;
    private RecyclerView.LayoutManager story_layoutManager;
    private StoryRecyclerAdapter story_adapter;
    private DatabaseReference db_reference;
    private boolean storyVisible = false;
    private FirebaseAuth mAuth;
    private String mFileName;
    private MediaRecorder mRecorder;
    private ImageButton micImageButton;
    private long startTimeRecord, endTimeRecord;
    private ImageButton profile_link_imageButton;
    private TextView xp_points_textView6;
    private Button logOutBtn;
    private TextView story_textView;
    BlurImageView blur_mic_image;
    BlurImageView blur_profile_link_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        story_textView = findViewById(R.id.story_textView);
        logOutBtn = findViewById(R.id.logOutBtn);
        xp_points_textView6 = findViewById(R.id.xp_points_textView6);
        profile_link_imageButton = findViewById(R.id.profile_link_imageButton);

        blur_mic_image = (BlurImageView) findViewById(R.id.blur_mic_image);
        blur_mic_image.setBlur(2);
        blur_profile_link_image = (BlurImageView) findViewById(R.id.blur_profile_link_image);
        blur_profile_link_image.setBlur(2);

        micImageButton = (ImageButton) findViewById(R.id.mic_imageButton);
        story_recyclerView = findViewById(R.id.stroy_recycleView);
        story_layoutManager = new GridLayoutManager(this, 1);
        story_recyclerView.setHasFixedSize(true);
        story_recyclerView.setLayoutManager(story_layoutManager);
        mAuth = FirebaseAuth.getInstance();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_Audio.3pg";


        blur_mic_image.setVisibility(View.GONE);
        blur_profile_link_image.setVisibility(View.GONE);

        story_recyclerView.setVisibility(View.GONE);
        db_reference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        story_hums = new ArrayList<Hum>();
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    story_hums.add(hum);
                }
                story_adapter = new StoryRecyclerAdapter(HomePageActivity.this, story_hums);
                story_recyclerView.setAdapter(story_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePageActivity.this, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        micImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                profile_link_imageButton.setVisibility(View.GONE);
                xp_points_textView6.setVisibility(View.GONE);
                logOutBtn.setVisibility(View.GONE);
                story_textView.setVisibility(View.GONE);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    startTimeRecord = SystemClock.uptimeMillis();
                    micImageButton.setImageResource(R.drawable.microphone_when_pressed);
                    //mRecordLabel.setText("Recording Started...");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //mRecordLabel.setText("Recording Stopped...");
                    micImageButton.setImageResource(R.drawable.mic_image);
                    stopRecording();
                    endTimeRecord = (int) (SystemClock.uptimeMillis() - startTimeRecord) / 1000;
                    //Toast.makeText(HomePageActivity.this, "Recording time is: "+endTimeRecord, Toast.LENGTH_LONG).show();
                    //uploadAudio("asaf", endTimeRecord);
                    startHumAcceptionActivity();
                }
                return false;
            }
        });
    }

    public void startHumAcceptionActivity()
    {
        Intent startHumAcceptionIntent = new Intent(getBaseContext(), HumAcceptionActivity.class);
        startHumAcceptionIntent.putExtra("End time record", endTimeRecord);
        startActivity(startHumAcceptionIntent);
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("fuck it", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void openStory(View view) {
        if(!storyVisible){
            story_recyclerView.setVisibility(View.VISIBLE);
            blur_mic_image.setVisibility(View.VISIBLE);
            blur_profile_link_image.setVisibility(View.VISIBLE);
            micImageButton.setVisibility(View.GONE);
            profile_link_imageButton.setVisibility(View.GONE);
            xp_points_textView6.setVisibility(View.GONE);
            logOutBtn.setVisibility(View.GONE);
            story_textView.setVisibility(View.GONE);
            storyVisible = true;

        }else if (storyVisible == true){
            blur_mic_image.setVisibility(View.GONE);
            blur_profile_link_image.setVisibility(View.GONE);

            story_recyclerView.setVisibility(View.GONE);
            micImageButton.setVisibility(View.VISIBLE);
            profile_link_imageButton.setVisibility(View.VISIBLE);
            xp_points_textView6.setVisibility(View.VISIBLE);
            logOutBtn.setVisibility(View.VISIBLE);
            story_textView.setVisibility(View.VISIBLE);
            storyVisible = false;
        }
    }

    public void launchMyProfilePage(View view) {
        Intent myProfileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileIntent);
    }

    public void LogOut(View view){
        mAuth.signOut();
        Intent HomePageIntent = new Intent(HomePageActivity.this, HomePageDemo.class);
        startActivity(HomePageIntent);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

