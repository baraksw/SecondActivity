package com.example.secondproject;


import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


public class Hum implements HumToDB {

    User user_viewed_list[];
    String owner, hum_id;
    int num_of_listeners, hum_len, hum_answered = View.INVISIBLE;
    String song_name;

    public Hum(String owner, String hum_id, int hum_len){
        this.hum_len = hum_len;
        this.hum_id = hum_id;
        this.owner = owner;
        this.num_of_listeners = 0;
        this.song_name = null ;
    }

    public String getOwner(){
        return owner;
    }

    public String getHum_id(){
        return hum_id;
    }


    public void playHum(){
        playAudio(this);
    }

    public static void OpenYoutubeOnWeb(String urlString) {
        //TODO: Iplementing the recyclerview
    }

    public int countNumOfListeners(){
        if(user_viewed_list == null){
            this.num_of_listeners = 0;
        } else {
            this.num_of_listeners = user_viewed_list.length;
        }
        return this.num_of_listeners;
    }

    public int getHum_len(){
        //TODO: updating this function;
        this.hum_len = 0;
        return this.hum_len;
    }

    public int getHum_answered(){
        return this.hum_answered;
    }

    //TODO: Create a function that set the answere of the hum.
    public void answereHum(String song_name){
        this.song_name = song_name;
        this.hum_answered = View.VISIBLE;
    }

    public void PlayAudio(Hum hum){}
}

