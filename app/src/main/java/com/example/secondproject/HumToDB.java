package com.example.secondproject;

import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public interface HumToDB {
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_Audio.3pg";
    int ANSWER_XP = 2;
    int UPLOAD_XP = -1;


    //Play a specific Hum
    default void playAudio(Hum hum) {
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

    //Insert new Hum object to realtime database
    default void addHumToDB(final Hum new_hum) {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("db2").child("hums_db").child(new_hum.getHum_id()).setValue(new_hum);
        mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Uploading hum decreases the XP value in 1
                mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").setValue(dataSnapshot.getValue(Integer.class) + UPLOAD_XP );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("logFail", "did not upload object");
            }
        });
    }


    //Upload answer of hum to DB
    default boolean UploadAnswer(String answer, Hum hum) {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("db2").child("hums_db").child(hum.getHum_id()).child("hum_answer").setValue(answer);
        mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataBase.child("DB").child("users_db").child(String.valueOf(mAuth.getCurrentUser().getDisplayName())).child("xp_cnt").setValue(dataSnapshot.getValue(Integer.class) + ANSWER_XP);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

}






