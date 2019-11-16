package com.example.basketballscorekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartMatch extends AppCompatActivity {
    EditText etTeam1, etTeam2;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        etTeam1 = findViewById(R.id.etTeam1);
        etTeam2 = findViewById(R.id.etTeam2);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTeam1.getText().toString().isEmpty() || etTeam2.getText().toString().isEmpty()) {
                    Toast.makeText(StartMatch.this, getString(R.string.input_fields_error), Toast.LENGTH_SHORT).show();
                }else {
                    String Team1 = etTeam1.getText().toString().trim();
                    String Team2 = etTeam2.getText().toString().trim();
                    Intent intent = new Intent(StartMatch.this, com.example.basketballscorekeeper.ScoreCard.class);
                    intent.putExtra("team1", Team1);
                    intent.putExtra("team2", Team2);
                    startActivity(intent);
                }
            }
        });
    }
}
