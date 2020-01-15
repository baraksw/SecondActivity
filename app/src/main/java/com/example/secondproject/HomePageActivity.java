package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.io.File;
import java.io.IOException;

public class HomePageActivity extends AppCompatActivity {

    private Button mRecordBtn;
    private TextView mRecordLabel;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private static final String LOG_TAG = "Record_log";
    private String audio_path;
    private String audio_file_random_name;
    private StorageReference mStorage;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Firebase.setAndroidContext(this);
        Firebase.setAndroidContext(this);
        mProgress = new ProgressDialog(this);
        mRecordLabel = (TextView) findViewById(R.id.recordLabel);
        mRecordBtn = (Button) findViewById(R.id.recordBtn);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";

        mStorage = FirebaseStorage.getInstance().getReference();

        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    mRecordLabel.setText("Recording Started...");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mRecordLabel.setText("Recording Stopped...");
                    stopRecording();
                }

                return false;
            }
        });


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
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        uploadAudio();
    }


    private void uploadAudio() {

        mProgress.setMessage("Uploading Audio...");
        mProgress.show();

        audio_file_random_name = UUID.randomUUID().toString();
        final StorageReference filepath = mStorage.child("Audio").child(audio_file_random_name);
        Uri uri = Uri.fromFile(new File(mFileName));
        audio_path = uri.getPath();
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                mRecordLabel.setText("Uploading Finished");
            }
        });
    }

    public void play_Audio(View v) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audio_path);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AuthPage(View view) {

        Intent AuthIntent = new Intent(this, AuthActivity.class);
        startActivity(AuthIntent);
    }
}



