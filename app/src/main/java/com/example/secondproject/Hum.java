package com.example.secondproject;


import android.content.Context;
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

    public void AddHum2Db(String answer, Context relevant_context) {
        this.num_of_hums_answered += 1;
        this.hum_answer = answer;

        boolean upload_success = UploadAnswer(answer, this);

        if (upload_success) {
            Toast.makeText(relevant_context, "Thanks for your answer!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(relevant_context, "Hum already answered...", Toast.LENGTH_LONG).show();
        }
    }

    public void print()
    {
        System.out.print("this id is: " + this.hum_id);
    }

    public int getNum_of_Hums_answered() {
        return this.num_of_hums_answered;
    }
}

