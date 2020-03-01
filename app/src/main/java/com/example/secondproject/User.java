package com.example.secondproject;

import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.interfaces.DHPrivateKey;

        public class User implements UserToDB {

            public String full_name = "anonymous", user_name = "anonymous user";
            public int xp_cnt = 0;
            public int level = 1;
            public int friends_number = 0;
            public int hums_number = 0;
            public HashMap<String,String> friends_map;

            static final int UPLOAD_XP = 1;
            static final int ANSWER_XP = 2;

            public User() {
                friends_map = new HashMap<String, String>();
            }
                
            public User(String full_name, String user_name)
            {
                friends_map = new HashMap<String, String>();
                this.full_name =  full_name;
                this.user_name = user_name;
            }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getXp_cnt() {
        return this.xp_cnt;
    }

    public void setXp_cnt(int xp_cnt) {
        this.xp_cnt = xp_cnt;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void updateLevel() {

        int new_level = this.xp_cnt / 100;

        if (new_level != this.level) {
            setLevel(new_level);
        }

    }

    public void addHum(Hum new_hum) {

        this.xp_cnt += 5;
        updateLevel();

        //published_hums[].add(new_hum);
    }


    public int get_friends_number() {
        return this.friends_number;
    }

    public void setFriends_number(int number) {
        friends_number = number;
    }

    public void add_friend(String name){
        friends_map.put(String.valueOf(friends_number),name);
        friends_number++;
    }

    public String get_friend(){
        return friends_map.get("0");

    }

    public void UpdateXp(int reason_code)
    {
        if(reason_code == 0) //Uploaded a hum
            setXp_cnt(this.xp_cnt + UPLOAD_XP );
        else
            setXp_cnt(this.xp_cnt + ANSWER_XP);
        update_XP_in_DB(this);
    }

}
