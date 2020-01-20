package com.example.secondproject;

import android.content.Intent;
import android.net.Uri;

import java.awt.*;

import java.net.URL;
import java.util.ArrayList;

public class Hum {

    //Add a reference to the Recorded file at the DB.
    User _user_viewed_list[], _owner;
    int num_of_listeners, hum_rec_len;
    String song_name, youtube_url;

    public Hum(int num){
        this.hum_rec_len = num;
        this.num_of_listeners = 0;
        this.song_name = "abc" + num ;
    }

    /*public Hum(User owner){
        //TODO: Saving the record file.
        _owner = owner;
    }*/

    private void play_hum(){
        //TODO: Implement the playing of the hum function.
    }

    private void add_user(User new_user){
        //TODO: Implement this method after Asaf will learn how to use firebase.
    }

    public void PlayHum(){
        //TODO: Implement the playing og the Hum from the firebase
    }

    public static void OpenYoutubeOnWeb(String urlString) {
        //TODO: Iplementing the recyclerview
    }
}
