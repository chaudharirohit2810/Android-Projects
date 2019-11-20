package com.example.floatingactionbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class Settings extends AppCompatActivity {
    TextView tvClearAll, tvClearCompleted;
    CheckBox DarkCheck;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!Splash.DarkTheme) {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvClearAll = findViewById(R.id.tvClearAll);
        tvClearCompleted = findViewById(R.id.tvClearCompleted);
        DarkCheck = findViewById(R.id.DarkCheck);
        DarkCheck.setChecked(Splash.DarkTheme);
        editor = getSharedPreferences(Splash.path, MODE_PRIVATE).edit();
        DarkCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DarkCheck.isChecked()) {
                    Splash.DarkTheme = true;
                    editor.putBoolean("Dark", true);
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    Splash.DarkTheme = false;
                    editor.putBoolean("Dark", false);
                    editor.commit();
                    setTheme(R.style.AppTheme);
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        tvClearCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskDB taskDB = new TaskDB(Settings.this);
                taskDB.open();
                taskDB.clearCompleted();
                taskDB.close();
                Toast.makeText(Settings.this, "Completed records cleared", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.this, MainActivity.class));
//                finish();
//                overridePendingTransition(1000, 1000);
//                startActivity(getIntent());
//                overridePendingTransition(1000, 1000);
            }
        });
        tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskDB taskDB = new TaskDB(Settings.this);
                taskDB.open();
                taskDB.clearAll();
                taskDB.close();
                Toast.makeText(Settings.this, "All the records cleared", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.this, MainActivity.class));
//                finish();
//                overridePendingTransition(1000, 1000);
//                startActivity(getIntent());
//                overridePendingTransition(1000, 1000);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
