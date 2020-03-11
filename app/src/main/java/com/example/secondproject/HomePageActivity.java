package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView storyRecyclerView;
    private ArrayList<Hum> storyHums;
    private RecyclerView.LayoutManager storyLayoutManager;
    private StoryRecyclerAdapter storyAdapter;
    private DatabaseReference dbReference;
    private boolean storyVisible = false;
    private FirebaseAuth mAuth;
    private String mFileName;
    private MediaRecorder mRecorder;
    private ImageButton micImageButton;
    private long startTimeRecord;
    private int endTimeRecord;
    private ImageButton profileLinkImageButton;
    private TextView xpPoints;
    private Button logOutBtn;
    private ProgressBar recordAnimation;
    private ProgressBar recordAnimation2;
    private ImageButton storyImageButton;
    private TextView storyTextView;
    BlurImageView blurMicImage;
    BlurImageView blurProfileLinkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        storyImageButton = findViewById(R.id.story_imageButton);
        storyTextView = findViewById(R.id.story_textView);
        logOutBtn = findViewById(R.id.logOutBtn);
        xpPoints = findViewById(R.id.xpPoints);
        profileLinkImageButton = findViewById(R.id.profile_link_imageButton);
        recordAnimation = findViewById(R.id.record_animation);
        recordAnimation2 = findViewById(R.id.record_animation2);
        blurMicImage = (BlurImageView) findViewById(R.id.blur_mic_image);
        blurMicImage.setBlur(2);
        blurProfileLinkImage = (BlurImageView) findViewById(R.id.blur_profile_link_image);
        blurProfileLinkImage.setBlur(2);
        micImageButton = (ImageButton) findViewById(R.id.mic_imageButton);
        storyRecyclerView = findViewById(R.id.stroy_recycleView);
        storyLayoutManager = new GridLayoutManager(this, 1);
        storyRecyclerView.setHasFixedSize(true);
        storyRecyclerView.setLayoutManager(storyLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_Audio.3pg";
        dbReference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        storyHums = new ArrayList<Hum>();

        blurMicImage.setVisibility(View.GONE);
        blurProfileLinkImage.setVisibility(View.GONE);
        recordAnimation.setVisibility(View.GONE);
        recordAnimation2.setVisibility(View.GONE);
        storyRecyclerView.setVisibility(View.GONE);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    storyHums.add(hum);
                }
                storyAdapter = new StoryRecyclerAdapter(HomePageActivity.this, storyHums);
                storyRecyclerView.setAdapter(storyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePageActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });

        micImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    startTimeRecord = SystemClock.uptimeMillis();
                    onPressedVisibilities();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    endTimeRecord = (int) (SystemClock.uptimeMillis() - startTimeRecord) / 1000;
                    onReleaseVisibilities();
                    startHumAcceptionActivity(endTimeRecord);
                }
                return false;
            }
        });
    }

    private void onPressedVisibilities() {
        recordAnimation.setVisibility(View.VISIBLE);
        recordAnimation2.setVisibility(View.VISIBLE);
        profileLinkImageButton.setVisibility(View.GONE);
        xpPoints.setVisibility(View.GONE);
        logOutBtn.setVisibility(View.GONE);
        storyTextView.setVisibility(View.GONE);
        storyImageButton.setVisibility(View.GONE);
    }


    private void onReleaseVisibilities() {
        recordAnimation.setVisibility(View.GONE);
        recordAnimation2.setVisibility(View.GONE);
        profileLinkImageButton.setVisibility(View.VISIBLE);
        xpPoints.setVisibility(View.VISIBLE);
        logOutBtn.setVisibility(View.VISIBLE);
        storyTextView.setVisibility(View.VISIBLE);
        storyImageButton.setVisibility(View.VISIBLE);
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
        if(!storyVisible){
            storyRecyclerView.setVisibility(View.VISIBLE);
            blurMicImage.setVisibility(View.VISIBLE);
            blurProfileLinkImage.setVisibility(View.VISIBLE);
            micImageButton.setVisibility(View.GONE);
            profileLinkImageButton.setVisibility(View.GONE);
            xpPoints.setVisibility(View.GONE);
            logOutBtn.setVisibility(View.GONE);
            storyTextView.setVisibility(View.GONE);
            storyVisible = true;
        }
        else if (storyVisible == true){
            blurMicImage.setVisibility(View.GONE);
            blurProfileLinkImage.setVisibility(View.GONE);
            storyRecyclerView.setVisibility(View.GONE);
            micImageButton.setVisibility(View.VISIBLE);
            profileLinkImageButton.setVisibility(View.VISIBLE);
            xpPoints.setVisibility(View.VISIBLE);
            logOutBtn.setVisibility(View.VISIBLE);
            storyTextView.setVisibility(View.VISIBLE);
            storyVisible = false;
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

