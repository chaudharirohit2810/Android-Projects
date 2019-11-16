package com.example.floatingactionbutton;

import android.app.Application;

import java.util.ArrayList;

public class subApplication extends Application {
    public static ArrayList<Task> data;
    @Override
    public void onCreate() {
        super.onCreate();
        data = new ArrayList<>();
    }
}
