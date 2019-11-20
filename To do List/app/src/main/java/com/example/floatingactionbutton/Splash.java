package com.example.floatingactionbutton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static String path = "com.example.floatingactionbutton.settings";
    public static boolean DarkTheme;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        preferences = getSharedPreferences(path, MODE_PRIVATE);
        Splash.DarkTheme = preferences.getBoolean("Dark", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
