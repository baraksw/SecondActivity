package com.example.secondproject;
import android.app.Application;

import com.firebase.client.Firebase;

public class AppClass extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
