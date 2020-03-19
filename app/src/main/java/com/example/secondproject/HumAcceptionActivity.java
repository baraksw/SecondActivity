package com.example.secondproject;

import android.app.Activity;
import android.view.View;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class HumAcceptionActivity extends Activity {

    private String record_path_FileName;
    private StorageReference hums_StorageRef;
    private int end_time_record;
    public HumsMap humsMap;
    private String current_username;
    private TextView record_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum_acception);
        humsMap = new HumsMap();
        hums_StorageRef = FirebaseStorage.getInstance().getReference();
        record_path_FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        record_path_FileName += "/recorded_Audio.3pg"; //a general name given locally to every record

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            current_username = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else
            Toast.makeText(HumAcceptionActivity.this, R.string.user_not_logged_in_msg, Toast.LENGTH_LONG).show();

        showHumLength();

    }

    private void showHumLength() {
        Intent homePageActivity = getIntent();
        end_time_record = homePageActivity.getIntExtra(getString(R.string.End_time_record_str), end_time_record);
        String recordLengthString = String.valueOf(end_time_record);
        record_length = (TextView)findViewById(R.id.current_record_length_textView);
        if(end_time_record <= 9)
            record_length.setText("0:0" + recordLengthString);
        else
            record_length.setText("0:" + recordLengthString);
    }

    //Play the current record
    public void recordPlayerOnClick(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(record_path_FileName); //Thats a general name given to every record, and it is overriden when
            //we start a new one. So by using record_path_FileName, you can hear the current record.
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
        Hum hum = new Hum(current_username, humID, end_time_record);
        uploadAudio(current_username, humID); //Uploading the audio file to firebase storage
        hum.addHumToDB(); //Uploading the Hum object to realtime database
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
            final StorageReference filepath = hums_StorageRef.child("Audio").child(username).child(hum_id);
            Uri uri = Uri.fromFile(new File(record_path_FileName));
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



