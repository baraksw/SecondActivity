package com.example.secondproject;

import android.content.Intent;
import android.net.Uri;

//import java.awt.*;

import java.net.URL;
import java.util.ArrayList;

public class Hum {

    //Add a reference to the Recorded file at the DB.
    User user_viewed_list[], owner;
    int num_of_listeners, hum_rec_len;
    String song_name, youtube_url;

    public Hum(int num){
        this.hum_rec_len = num;
        this.num_of_listeners = 0;
        this.song_name = "abc" + num ;
    }

    /*public Hum(User owner){
        //TODO: Saving the record file.
        //TODO: Set an חח"ע id for the hum.
        this.owner = owner;
    }*/

    private void play_hum(){
        //TODO: Implement the playing of the hum function.
    }



    public void PlayHum(){
        //TODO: Implement the playing og the Hum from the firebase
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
}
