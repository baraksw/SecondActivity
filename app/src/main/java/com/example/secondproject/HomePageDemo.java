package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePageDemo extends AppCompatActivity {

    private Button mRecordBtn;
    private Button mPlayRecordBtn;
    private TextView mRecordLabel;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private static final String LOG_TAG = "Record_log";
    private String audio_path;
    private String audio_file_random_name;
    private String hum_id;
    private String username = "asaf";
    private long startTimeRecord;
    private int endTimeRecord;
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private Firebase mRef;
    private TextView mValue;
    private User current_user;
    private UsersMap users_map;
    private String user_path;
    private String db_path = "https://secondproject-a6fe3.firebaseio.com/Users/";
    private DatabaseReference mDataBase, mDataBaseHums;

    public HumsMap hums_map;

    //Sample hum to work with for now - it should be replaced with a given hum from RecyclerView!
    Hum hum = new Hum("asaf", "200120_1947.78cb09b4-2c12-48b2-b215-b1aa5293ac14", 5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_demo);
        Firebase.setAndroidContext(this);
        mProgress = new ProgressDialog(this);
        mRecordLabel = (TextView) findViewById(R.id.recordLabel);
        mRecordBtn = (Button) findViewById(R.id.recordBtn);
        mPlayRecordBtn = (Button) findViewById(R.id.button_play_current_record);
        mStorage = FirebaseStorage.getInstance().getReference();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_Audio.3pg";
        mDataBase = FirebaseDatabase.getInstance().getReference();
        users_map = new UsersMap();
        current_user = new User();
        hums_map = new HumsMap();

        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    mRecordLabel.setText("Recording Started...");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mRecordLabel.setText("Recording Stopped...");
                    stopRecording();
                    endTimeRecord = (int) (SystemClock.uptimeMillis() - startTimeRecord) / 1000;
                    //Toast.makeText(HomePageDemo.this, "Recording time is: "+endTimeRecord, Toast.LENGTH_LONG).show();
                    uploadAudio(username, endTimeRecord);
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
        startTimeRecord = SystemClock.uptimeMillis();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void uploadAudio(String username, int endTimeRecord) {

        mProgress.setMessage("Uploading Audio...");
        mProgress.show();

        audio_file_random_name = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
        String currentDateTime = sdf.format(new Date());
        hum_id = currentDateTime + "_" + audio_file_random_name;

        Hum new_hum = new Hum(username, hum_id, endTimeRecord);
        Toast.makeText(HomePageDemo.this, "Recording time is: " + hum_id, Toast.LENGTH_LONG).show();
        //restart_DB();
        add_hum_to_db(new_hum);


        final StorageReference filepath = mStorage.child("Audio").child(username).child(hum_id);
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


    public void playBtn_OnClick(View view) {
        playAudio(hum);
    }

    public void playAudio(Hum hum) {

        final MediaPlayer mediaPlayer = new MediaPlayer();
        final StorageReference filepath = mStorage.child("Audio").child(hum.getOwner()).child(hum.getHum_id());
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    mediaPlayer.setDataSource(uri.toString());
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void AuthPage(View view) {

        Intent AuthIntent = new Intent(this, LoginActivity.class);
        startActivity(AuthIntent);
    }

    public void add_hum_to_db(final Hum new_hum) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    {
                        hums_map = dataSnapshot.child("db2").getValue(HumsMap.class);
                        Hum hummy = new Hum("tomer", "efuef", 3);
                        hums_map.add_hum(new_hum);
                        mDataBase.child("db2").setValue(hums_map);
                    }
                } catch (Exception e) {
                    Log.w("exception", "fuck it");
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




/*
    public void register(View view) {
        mDataBase.child("DB/users_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user.set_user_name("Test_user");
                current_user.set_full_name("New Test");
                users_map.add_user(current_user);
                mDataBase.child("NEW_DB").setValue(users_map);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

 */

    public void register(View view) {
        current_user.setUser_name("Test_user3");
        current_user.setFull_name("New Test3");
        //add_user_to_db(current_user);
    }


    public void onClick_play_current_record(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(mFileName); //Thats a general name given to every record, and is overrided when
            //we start a new one. So using this name, you can here the current record.
            player.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void restart_DB() {
        hums_map = new HumsMap();
        Hum fake_hum = new Hum("asaf", "4ddd545", 5);
        hums_map.add_hum(fake_hum);
        Hum fake_hum2 = new Hum("tomer", "4ddd5488", 5);
        hums_map.add_hum(fake_hum2);
        mDataBase.child("db2").setValue(hums_map);
        Toast.makeText(HomePageDemo.this, "hums_DB Restarted", Toast.LENGTH_SHORT).show();
    }


}
