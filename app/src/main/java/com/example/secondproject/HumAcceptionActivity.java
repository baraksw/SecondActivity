package com.example.secondproject;

import android.app.Activity;
import android.view.View;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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



public class HumAcceptionActivity extends Activity {


    private FirebaseAuth mAuth;
    private FirebaseAuth humAuth;
    private String humFileName;
    private String audio_path;
    private ProgressDialog humProgress;
    private StorageReference humStorage;
    private int endTimeRecord;
    private ImageView micImageButton;
    private DatabaseReference mDataBase;
    public HumsMap hums_map;
    private String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum_acception);
        humAuth = FirebaseAuth.getInstance();
        hums_map = new HumsMap();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        humProgress = new ProgressDialog(this);
        humStorage = FirebaseStorage.getInstance().getReference();
        Intent home_page_activity = getIntent();
        humFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        humFileName += "/recorded_Audio.3pg";
        endTimeRecord = home_page_activity.getIntExtra("End time record", endTimeRecord);
        mAuth = FirebaseAuth.getInstance();
        current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }


    public void playCurrentRecord(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(humFileName); //Thats a general name given to every record, and is overrided when
            //we start a new one. So by using humFileName, you can hear the current record.
            player.prepare();
        } catch (IOException e) {
            Log.e("PlayCurrent_log", "prepare() failed");
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void resetHumRecording(View view) {
        Intent home_page_intent = new Intent(this, HomePageActivity.class);
        Toast.makeText(HumAcceptionActivity.this, "Succecfuly uploaded!", Toast.LENGTH_SHORT).show();
        startActivity(home_page_intent);
    }

    public void upload2DB(View view) {

        String audio_file_random_name = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
        String currentDateTime = sdf.format(new Date());
        String hum_id = currentDateTime + "_" + audio_file_random_name;
        mDataBase.child("DB").child("users_db").child(current_user).child("Hums").child("hums0").setValue(hum_id);
        try {
            Hum hum = new Hum(current_user, hum_id, endTimeRecord);
            uploadAudio(current_user, endTimeRecord, hum_id);
            add_hum_to_db(hum);

        } catch (Exception e) {
            Log.w("fuck it", "bubi");
            e.printStackTrace();
        }

        Intent home_page_intent = new Intent(this, HomePageActivity.class);
        startActivity(home_page_intent);

    }

    private void uploadAudio(String username, int endTimeRecord, String hum_id) {

        humProgress.setMessage("Uploading Audio...");
        humProgress.show();

        //Hum new_hum = new Hum(username, hum_id, endTimeRecord);
        //Toast.makeText(HomePageDemo.this, "Recording time is: " + hum_id, Toast.LENGTH_LONG).show();
        //restart_DB();
        //add_hum_to_db(new_hum);

        try {
            final StorageReference filepath = humStorage.child("Audio").child(username).child(hum_id);
            Uri uri = Uri.fromFile(new File(humFileName));
            audio_path = uri.getPath();
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    humProgress.dismiss();
                    Toast.makeText(HumAcceptionActivity.this, "Succecfuly uploaded!", Toast.LENGTH_SHORT).show();
                }

                //mRecordLabel.setText("Uploading Finished");
            });
        } catch (Exception e) {
            Log.w("fuck it", "bubi1");
            e.printStackTrace();

        }
    }

    public void add_hum_to_db(final Hum new_hum) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    {
                        hums_map = dataSnapshot.child("db2").getValue(HumsMap.class);
                        hums_map.add_hum(new_hum);
                        mDataBase.child("db2").setValue(hums_map);
                        /*users_map = dataSnapshot.child("DB").getValue(UsersMap.class);
                        current_user = users_map.getUser("asaf");
                        //current_user.add_hum(hum_id);
                        mDataBase.child("DB").setValue(users_map);
                        */

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

}



