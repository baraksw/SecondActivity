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

    public Hum(String owner, String hum_id, int hum_len) {
        this.hum_len = hum_len;
        this.hum_id = hum_id;
        this.owner = owner;
        this.num_of_listeners = 0;
        this.song_name = null;
    }

    public Hum()
    {
        this.owner = "nobody";
        this.hum_id = "45tts";


    }
    public String getOwner() {
        return owner;
    }

    public String getHum_id() {
        return hum_id;
    }


    public void playHum() {
        playAudio(this);
    }

    public void add_hum_to_db() {
        //add_hum_to_db(this);
    }

    public static void OpenYoutubeOnWeb(String urlString) {
        //TODO: Implementing the recyclerview
    }

    public int countNumOfListeners() {
        if (user_viewed_list == null) {
            this.num_of_listeners = 0;
        } else {
            this.num_of_listeners = user_viewed_list.length;
        }
        return this.num_of_listeners;
    }

    public int getHum_len() {
        //TODO: updating this function;
        return this.hum_len;
    }

    public void print()
    {
        System.out.print("this id is: " + this.hum_id);
    }

    public int getHum_answered() {
        return this.hum_answered;
    }

    //TODO: Create a function that set the answere of the hum.
    public void answereHum(String song_name) {
        this.song_name = song_name;
        this.hum_answered = View.VISIBLE;
    }

}

