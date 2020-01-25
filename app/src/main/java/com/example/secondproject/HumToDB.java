package com.example.secondproject;

import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface HumToDB {
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

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
}
