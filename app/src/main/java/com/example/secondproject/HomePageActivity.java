package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;

import java.io.IOException;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private RecyclerView story_RecyclerView;
    private ArrayList<Hum> story_hums;
    private RecyclerView.LayoutManager story_LayoutManager;
    private StoryRecyclerAdapter story_Adapter;
    private DatabaseReference hums_dbReference;
    private boolean is_story_visible = false;
    private FirebaseAuth mAuth;
    private String record_path_FileName;
    private MediaRecorder mRecorder;
    private long start_time_record;
    private int end_time_record;
    private ImageButton mic_ImageButton;
    private ImageButton profile_link_ImageButton;
    private ImageButton story_ImageButton;
    private TextView xp_points_TextView;
    private TextView story_TextView;
    private Button log_out_Button;
    private ProgressBar record_animation;
    private ProgressBar record_animation2;
    BlurImageView mic_BlurImage;
    BlurImageView profile_link_BlurImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        story_ImageButton = findViewById(R.id.story_imageButton);
        story_TextView = findViewById(R.id.story_textView);
        log_out_Button = findViewById(R.id.logOutBtn);
        xp_points_TextView = findViewById(R.id.xpPoints);
        profile_link_ImageButton = findViewById(R.id.profile_link_imageButton);
        record_animation = findViewById(R.id.record_animation);
        record_animation2 = findViewById(R.id.record_animation2);
        mic_BlurImage = (BlurImageView) findViewById(R.id.blur_mic_image);
        mic_BlurImage.setBlur(2);
        profile_link_BlurImage = (BlurImageView) findViewById(R.id.blur_profile_link_image);
        profile_link_BlurImage.setBlur(2);
        mic_ImageButton = (ImageButton) findViewById(R.id.mic_imageButton);
        story_RecyclerView = findViewById(R.id.stroy_recycleView);
        story_LayoutManager = new GridLayoutManager(this, 1);
        story_RecyclerView.setHasFixedSize(true);
        story_RecyclerView.setLayoutManager(story_LayoutManager);
        mAuth = FirebaseAuth.getInstance();
        record_path_FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        record_path_FileName += "/recorded_Audio.3pg"; //a general name given locally to every record
        hums_dbReference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        story_hums = new ArrayList<Hum>();

        mic_BlurImage.setVisibility(View.GONE);
        profile_link_BlurImage.setVisibility(View.GONE);
        record_animation.setVisibility(View.GONE);
        record_animation2.setVisibility(View.GONE);
        story_RecyclerView.setVisibility(View.GONE);

        //Creating the Hum's story
        hums_dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                story_hums.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    story_hums.add(hum);
                }
                story_Adapter = new StoryRecyclerAdapter(HomePageActivity.this, story_hums);
                story_RecyclerView.setAdapter(story_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePageActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });

        //Recording a hum
        mic_ImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    start_time_record = SystemClock.uptimeMillis();
                    micOnPressedVisibilities();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    end_time_record = (int) (SystemClock.uptimeMillis() - start_time_record) / 1000;
                    micOnReleaseVisibilities();
                    startHumAcceptionActivity(end_time_record);
                }
                return false;
            }
        });
    }

    private void micOnPressedVisibilities() {
        record_animation.setVisibility(View.VISIBLE);
        record_animation2.setVisibility(View.VISIBLE);
        profile_link_ImageButton.setVisibility(View.GONE);
        xp_points_TextView.setVisibility(View.GONE);
        log_out_Button.setVisibility(View.GONE);
        story_TextView.setVisibility(View.GONE);
        story_ImageButton.setVisibility(View.GONE);
    }


    private void micOnReleaseVisibilities() {
        record_animation.setVisibility(View.GONE);
        record_animation2.setVisibility(View.GONE);
        profile_link_ImageButton.setVisibility(View.VISIBLE);
        xp_points_TextView.setVisibility(View.VISIBLE);
        log_out_Button.setVisibility(View.VISIBLE);
        story_TextView.setVisibility(View.VISIBLE);
        story_ImageButton.setVisibility(View.VISIBLE);
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(record_path_FileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("logFail", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void startHumAcceptionActivity(int endTimeRecord)
    {
        Intent startHumAcceptionIntent = new Intent(getBaseContext(), HumAcceptionActivity.class);
        startHumAcceptionIntent.putExtra(getString(R.string.End_time_record_str), endTimeRecord);
        startActivity(startHumAcceptionIntent);
    }

    public void onClickOpenStory(View view) {
        if(!is_story_visible){
            story_RecyclerView.setVisibility(View.VISIBLE);
            mic_BlurImage.setVisibility(View.VISIBLE);
            profile_link_BlurImage.setVisibility(View.VISIBLE);
            mic_ImageButton.setVisibility(View.GONE);
            profile_link_ImageButton.setVisibility(View.GONE);
            xp_points_TextView.setVisibility(View.GONE);
            log_out_Button.setVisibility(View.GONE);
            story_TextView.setVisibility(View.GONE);
            is_story_visible = true;
        }
        else if (is_story_visible == true){
            mic_BlurImage.setVisibility(View.GONE);
            profile_link_BlurImage.setVisibility(View.GONE);
            story_RecyclerView.setVisibility(View.GONE);
            mic_ImageButton.setVisibility(View.VISIBLE);
            profile_link_ImageButton.setVisibility(View.VISIBLE);
            xp_points_TextView.setVisibility(View.VISIBLE);
            log_out_Button.setVisibility(View.VISIBLE);
            story_TextView.setVisibility(View.VISIBLE);
            is_story_visible = false;
        }
    }

    public void launchMyProfilePage(View view) {
        Intent myProfileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileIntent);
    }

    public void LogOut(View view){
        mAuth.signOut();
        Intent HomePageIntent = new Intent(HomePageActivity.this, LoginActivity.class);
        startActivity(HomePageIntent);
    }
}

