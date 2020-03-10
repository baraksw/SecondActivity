package com.example.secondproject;

import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.interfaces.DHPrivateKey;

public class User implements UserToDB {

    public String Friends = "My Friends";
    public String Hums = "My Hums";
    public String full_name = "anonymous", user_name = "anonymous user";
    public int xp_cnt = 3;
    public int level = 1;
    public int friends_number = 0;

    public User() {

    }

    public User(String full_name, String user_name) {
        this.full_name = full_name;
        this.user_name = user_name;
    }

    public User(String full_name, String user_name, int xp_cnt) {
        this.full_name = full_name;
        this.user_name = user_name;
        this.xp_cnt = xp_cnt;

    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public int getXp_cnt() {
        //return getXP(this);
        return 1;
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

    public int get_friends_number() {
        return this.friends_number;
    }

    public void setFriends_number(int number) {
        friends_number = number;
    }

    public void add_friend(String name) {
        friends_number++;
    }


}
