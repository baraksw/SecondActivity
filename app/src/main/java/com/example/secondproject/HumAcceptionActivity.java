package com.example.secondproject;

import android.app.Activity;
import android.view.View;
import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    private String humFileName;
    private StorageReference humStorage;
    private int endTimeRecord;
    private DatabaseReference mDataBase;
    public HumsMap humsMap;
    private String currentUser;
    private TextView recordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum_acception);
        humsMap = new HumsMap();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        humStorage = FirebaseStorage.getInstance().getReference();
        humFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        humFileName += "/recorded_Audio.3pg";

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            currentUser = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else
            Toast.makeText(HumAcceptionActivity.this, R.string.user_not_logged_in_msg, Toast.LENGTH_LONG).show();

        showHumLength();

    }

    private void showHumLength() {
        Intent homePageActivity = getIntent();
        endTimeRecord = homePageActivity.getIntExtra(getString(R.string.End_time_record_str), endTimeRecord);
        String recordLengthString = String.valueOf(endTimeRecord);
        recordLength = (TextView)findViewById(R.id.current_record_length_textView);
        if(endTimeRecord <= 9)
            recordLength.setText("0:0" + recordLengthString);
        else
            recordLength.setText("0:" + recordLengthString);
    }

    public void humPlayerOnClick(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(humFileName); //Thats a general name given to every record, and is overrided when
            //we start a new one. So by using humFileName, you can hear the current record.
            player.prepare();
        } catch (IOException e) {
            Log.e("logPlay", "prepare() failed");
        }
        player.start();
    }


    public void resetHumRecording(View view) {
        Intent home_page_intent = new Intent(this, HomePageActivity.class);
        Toast.makeText(HumAcceptionActivity.this, R.string.cancel_record_msg, Toast.LENGTH_SHORT).show();
        startActivity(home_page_intent);
    }

    public void uploadToDB(View view) {

        String humID = createNewHumID();
        Hum hum = new Hum(currentUser, humID, endTimeRecord);
        uploadAudio(currentUser, humID); //Uploading the Hum audio file to storage
        hum.addHumToDB(); //Uploading the Hum object to database
        startActivity(new Intent(this, HomePageActivity.class));
    }

    public String createNewHumID() {
        String audio_file_random_name = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
        String currentDateTime = sdf.format(new Date());
        String humID = currentDateTime + "_" + audio_file_random_name;

        return humID;
    }

    private void uploadAudio(String username, String hum_id) {
        try {
            final StorageReference filepath = humStorage.child("Audio").child(username).child(hum_id);
            Uri uri = Uri.fromFile(new File(humFileName));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(HumAcceptionActivity.this, R.string.upload_success_msg, Toast.LENGTH_SHORT).show();
                }

            });
        } catch (Exception e) {
            Log.w("logFail", "did not upload audio file");
            e.printStackTrace();

        }
    }
}



