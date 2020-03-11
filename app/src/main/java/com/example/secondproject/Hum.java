package com.example.secondproject;

import android.content.Context;
import android.view.View;
import android.widget.Toast;


public class Hum implements HumToDB {

    String owner, hum_id;
    int num_of_listeners = 0, hum_len= 0, num_of_hums_answered =0 , answered = View.INVISIBLE;
    String hum_answer = "NULL";

    public Hum(String owner, String hum_id, int hum_len) {
        this.hum_len = hum_len;
        this.hum_id = hum_id;
        this.owner = owner;
        this.num_of_listeners = 0;
        this.num_of_hums_answered = 0;
    }

    public Hum()
    {
        this.owner = "nobody";
        this.hum_id = "null";
    }

    public String getOwner() {
        return owner;
    }

    public String getHum_id() {
        return hum_id;
    }

    public String getHum_answer() { return hum_answer; }

    public int getAnswered() {
        return answered;
    }

    public void playHum() {
        playAudio(this);
    }

    public int getHum_len() {
        //TODO: updating this function;
        return this.hum_len;
    }

    public void AddAnswerToDB(String answer, Context relevant_context) {
        this.num_of_hums_answered += 1;
        this.hum_answer = answer;
        this.answered = View.VISIBLE;

        boolean upload_success = UploadAnswer(answer, this);

        if (upload_success) { //if nobody has answered this hum before
            Toast.makeText(relevant_context, R.string.upload_answer_success_msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(relevant_context, R.string.upload_answer_fail_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void addHumToDB(){
        addHumToDB(this); //Implemented in HumToDB interface
    }

}

