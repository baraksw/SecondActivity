package com.example.secondproject;


import android.view.View;
import android.widget.Toast;


public class Hum implements HumToDB {

    User user_viewed_list[];
    String owner, hum_id;
    int num_of_listeners, hum_len, num_of_hums_answered;
    String hum_answer;

    public Hum(String owner, String hum_id, int hum_len) {
        this.hum_len = hum_len;
        this.hum_id = hum_id;
        this.owner = owner;
        this.num_of_listeners = 0;
        this.hum_answer = null;
        num_of_hums_answered = 0;
        user_viewed_list = null;
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

    public String getHum_answer() { return hum_answer; }

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

    public boolean uploadAnswer(String answer) {
        return UploadAnswer(answer, this);
    }

    public void print()
    {
        System.out.print("this id is: " + this.hum_id);
    }

    public int getNum_of_Hums_answered() {
        return this.num_of_hums_answered;
    }

    public void setHumAnswer(String song_name) {
        this.hum_answer = song_name;
        //this.num_of_hums_answered = View.VISIBLE;
    }

}

