package com.example.basketballscorekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    RecyclerView rvList;
    Button btnClear;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<Match> matches;
    MatchDB matchDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvList = findViewById(R.id.rvList);
        btnClear = findViewById(R.id.btnClear2);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.hasFixedSize();
        try {
            matchDB = new MatchDB(History.this);
            matchDB.open();
            matches = matchDB.getData();
            matchDB.close();
        } catch (SQLException e) {
            Toast.makeText(History.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        adapter = new MatchAdapter(this, matches);
        rvList.setAdapter(adapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDB matchDB = new MatchDB(History.this);
                matchDB.open();
                matchDB.clearAll();
                Toast.makeText(History.this, "Records cleared successfully !!", Toast.LENGTH_SHORT).show();
                matchDB.close();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }


}
