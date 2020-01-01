package com.example.secondproject;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

public class User {

    String _full_name, _user_name;
    int _xp_cnt = 0, _level = 1;
    Hum _published_hums[];

    public User(String full_name, String user_name){
        _full_name = full_name;
        _user_name = user_name;
    }

    private String get_full_name() {
        return _full_name;
    }

    private String get_user_name() {
        return _user_name;
    }

    private Hum[] get_published_hums() {
        return _published_hums;
    }

    private Hum get_publish_hums(int index){
        return _published_hums[index];
    }

    private int how_many_hums(){
        return _published_hums.length();
    }
}
