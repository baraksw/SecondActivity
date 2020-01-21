package com.example.secondproject;

import java.util.HashMap;
import java.util.Map;
import com.example.secondproject.User;

public class UsersMap {
    public HashMap<String,User> users_db;

    public UsersMap(){
        users_db = new HashMap<String, User>();
    }

    public HashMap<String, User> getUsers_db() {
        return users_db;
    }

    public void setUsers_db(HashMap<String, User> users_db) {
        this.users_db = users_db;
    }

    public void add_user(User user){
        users_db.put(user.getFull_name(),user);
    }

    public void remove_user(User user){users_db.remove(user.getFull_name());}

    public void clear(){
        users_db.clear();
    }

}
