package com.example.secondproject;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class User {

    public String full_name, user_name;
    int xp_cnt = 0, level = 1;
    Hum published_hums[];

    public User() {
        this.full_name = "annonymous";
        this.user_name = "annonymous_user";
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

        published_hums[].add(new_hum);
    }
}
