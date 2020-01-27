package com.example.secondproject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.interfaces.DHPrivateKey;

public class User implements UserToDB{

    private String full_name = "anonymous", user_name = "anonymous user";
    private int xp_cnt = 0, level = 1;
    private Hum published_hums[];
    private String friends_array [];
    private int friends_number;
    private String hums_array [];
    private int hums_number;

    public User() {
        friends_array = new String[10];
        friends_number=0;
        hums_array = new String[10];
        hums_number=0;
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

    public int getXp_cnt(){
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

    public Hum[] getPublished_hums() {
        return this.published_hums;
    }

    public Hum getHum_at_index(int index){
        return this.published_hums[index];
    }

    public void updateLevel(){

        int new_level = this.xp_cnt/100;

        if (new_level != this.level){
            setLevel(new_level);
        }
    }

    public void addHum(Hum new_hum){

        this.xp_cnt += 5;
        updateLevel();

        //published_hums[].add(new_hum);
    }


    public void add_friend(String name)
    {
        this.friends_array[this.friends_number]=name;
        friends_number++;
    }

    public String get_friend(int num)
    {
        return this.friends_array[num];
    }

    public void add_hum(String hum_id){
        this.hums_array[this.hums_number] = hum_id;
        hums_number++;
    }

    public void add_friends(){

    }


}
