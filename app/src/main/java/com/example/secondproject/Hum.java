package com.example.secondproject;

import java.util.ArrayList;

public class Hum {

    //Add a reference to the Recorded file at the DB.
    User _user_viewed_list[], _owner;
    int num_of_listeners, hum_rec_len;

    public Hum(int num){
        this.hum_rec_len = num;
        this.num_of_listeners = 0;
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

}
