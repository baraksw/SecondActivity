package com.example.secondproject;

import android.app.Activity;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.toIntExact;
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
    private String recordLengthString;
    private ImageView micImageButton;
    private DatabaseReference mDataBase;
    public HumsMap hums_map;
    private String current_user;
    private TextView recordLength;
    private ImageButton playerBtn;
    private boolean play = false;

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
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            current_user = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else
            Toast.makeText(HumAcceptionActivity.this, "User not logged in!!",Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        endTimeRecord = home_page_activity.getIntExtra("End time record", endTimeRecord);
        String recordLengthString = String.valueOf(endTimeRecord);
        recordLength = (TextView)findViewById(R.id.current_record_length_textView);
        //Toast.makeText(HumAcceptionActivity.this, "Recording time is: "+ endTimeRecord, Toast.LENGTH_LONG).show();
        if(endTimeRecord <= 9)
            recordLength.setText("0:0" + recordLengthString);
        else
            recordLength.setText("0:" + recordLengthString);
        playerBtn = findViewById(R.id.play_current_record_imageButton);
    }

    public void humPlayerOnClick(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(humFileName); //Thats a general name given to every record, and is overrided when
            //we start a new one. So by using humFileName, you can hear the current record.
            player.prepare();
        } catch (IOException e) {
            Log.e("PlayCurrent_log", "prepare() failed");
        }

        player.start();
    }


    public void resetHumRecording(View view) {
        Intent home_page_intent = new Intent(this, HomePageActivity.class);
        Toast.makeText(HumAcceptionActivity.this, "Canceld recording", Toast.LENGTH_SHORT).show();
        startActivity(home_page_intent);
    }

    public void upload2DB(View view) {

        String audio_file_random_name = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
        String currentDateTime = sdf.format(new Date());
        String hum_id = currentDateTime + "_" + audio_file_random_name;
        Hum hum = new Hum(current_user, hum_id, endTimeRecord);
        uploadAudio(current_user, endTimeRecord, hum_id);
        add_hum_to_db(hum);
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
        mDataBase.child("db2").child("hums_db").child(new_hum.hum_id).setValue(new_hum);
        mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").setValue(dataSnapshot.getValue(Integer.class)-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



