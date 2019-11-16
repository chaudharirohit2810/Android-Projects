package com.example.basketballscorekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreCard extends AppCompatActivity {
    TextView tvTeam1Name, tvTeam2Name;
    EditText etScore1, etScore2;
    Button btnTeam1_1, btnTeam1_2, btnTeam1_3, btnTeam2_1, btnTeam2_2, btnTeam2_3, btnSave, btnReset;
    int score1, score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card);

        tvTeam1Name = findViewById(R.id.tvTeam1Name);
        tvTeam2Name = findViewById(R.id.tvTeam2Name);
        etScore1 = findViewById(R.id.etScore1);
        etScore2 = findViewById(R.id.etScore2);
        btnTeam1_1 = findViewById(R.id.btnTeam1_1);
        btnTeam1_2 = findViewById(R.id.btnTeam1_2);
        btnTeam1_3 = findViewById(R.id.btnTeam1_3);
        btnTeam2_1 = findViewById(R.id.btnTeam2_1);
        btnTeam2_2 = findViewById(R.id.btnTeam2_2);
        btnTeam2_3 = findViewById(R.id.btnTeam2_3);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);

        final String Team1 = getIntent().getStringExtra("team1");
        final String Team2 = getIntent().getStringExtra("team2");
        tvTeam1Name.setText(Team1);
        tvTeam2Name.setText(Team2);

        //To add 1 to score of Team 1
        btnTeam1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score1 = Integer.parseInt(etScore1.getText().toString().trim());
                score1++;
                etScore1.setText(Integer.toString(score1));
            }
        });

        //To add 2 to score of Team 1
        btnTeam1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score1 = Integer.parseInt(etScore1.getText().toString().trim());
                score1 = score1 + 2;
                etScore1.setText(Integer.toString(score1));
            }
        });

        //To add 3 to score of Team 1
        btnTeam1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score1 = Integer.parseInt(etScore1.getText().toString().trim());
                score1 = score1 + 3;
                etScore1.setText(Integer.toString(score1));
            }
        });

        //To add 1 to score of Team 2
        btnTeam2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score2 = Integer.parseInt(etScore2.getText().toString().trim());
                score2++;
                etScore2.setText(Integer.toString(score2));
            }
        });

        //To add 2 to score of Team 2
        btnTeam2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score2 = Integer.parseInt(etScore2.getText().toString().trim());
                score2 = score2 + 2;
                etScore2.setText(Integer.toString(score2));
            }
        });

        //To add 3 to score of Team 2
        btnTeam2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score2 = Integer.parseInt(etScore2.getText().toString().trim());
                score2 = score2 + 3;
                etScore2.setText(Integer.toString(score2));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Winner;
                if(Integer.parseInt(etScore1.getText().toString()) > Integer.parseInt(etScore2.getText().toString())) {
                    Winner = Team1 + " won the match !!";
                }else if(Integer.parseInt(etScore1.getText().toString()) < Integer.parseInt(etScore2.getText().toString())) {
                    Winner = Team2 + " won the match !!";
                }else {
                    Winner =  "Match Drawn !!";
                }
                try {
                    MatchDB matchDB = new MatchDB(ScoreCard.this);
                    matchDB.open();
                    matchDB.createEntry(Team1, Team2, etScore1.getText().toString(), etScore2.getText().toString(), Winner);
                } catch (SQLException e) {
                    Toast.makeText(ScoreCard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(ScoreCard.this, Winner, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ScoreCard.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etScore1.setText("0");
                etScore2.setText("0");
            }
        });
    }
}
