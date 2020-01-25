package com.example.secondproject;

import android.content.Intent;
import android.net.Uri;

//import java.awt.*;

import java.net.URL;
import java.util.ArrayList;

public class Hum {

    User _user_viewed_list[];
    String _owner, _hum_id;
    int num_of_listeners, hum_len;
    String song_name, youtube_url;

    public Hum(String owner, String hum_id, int hum_len){
        this.hum_len = hum_len;
        this._hum_id = hum_id;
        this._owner = owner;
        this.num_of_listeners = 0;
        this.song_name = "" ;
    }

    /*public Hum(User owner){
        //TODO: Saving the record file.
        //TODO: Set an חח"ע id for the hum.
        _owner = owner;
    }*/

    public String getOwner(){
        return _owner;
    }

    public String get_hum_id(){
        return _hum_id;
    }
    private void play_hum(){
        //TODO: Implement the playing of the hum function.
    }



    public void PlayHum(){
        //TODO: Implement the playing og the Hum from the firebase
    }

    public static void OpenYoutubeOnWeb(String urlString) {
        //TODO: Iplementing the recyclerview
    }

    public int getNum_of_listeners(){
        if(this._user_viewed_list == null){
            this.num_of_listeners = 0;
        } else {
            this.num_of_listeners = this._user_viewed_list.length;
        }

        return this.num_of_listeners;
    }

    public int getHum_len(){
        //TODO: updating this function;
        this.hum_len = 0;
        return this.hum_len;
    }
}
