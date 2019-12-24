package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tv_player1, tv_player2;
    Button btn_reset;
    Button[][] blocks = new Button[3][3];
    boolean player1_turn = true;
    int player1_wins = 0, player2_wins = 0;
    int i, j;
    private static final String TAG = "MainActivityTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_player1 = findViewById(R.id.main_tv_player1);
        tv_player2 = findViewById(R.id.main_tv_player2);

        tv_player1.setText("Player 1:  " + player1_wins);
        tv_player2.setText("Player 2:  " + player2_wins);

        btn_reset = findViewById(R.id.main_btn_reset);

        //To get ids of all the button in blocks
        for(i = 0;i < 3; i++) {
            for(j = 0;j < 3; j++) {
                String name = "main_btn_" + i + j;
                int id = getResources().getIdentifier(name, "id", getPackageName());
                blocks[i][j] = findViewById(id);
                blocks[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(((Button) view).getText().equals("")) {
                            if (player1_turn) {
                                ((Button) view).setText("X");
                            } else {
                                ((Button) view).setText("O");
                            }
                            if(checkForWin()) {
                                if(player1_turn) {
                                    player1_wins++;
                                    showMessage("Player 1 Won");
                                    resetBoard();
                                }else {
                                    player2_wins++;
                                    showMessage("Player 2 Won");
                                    resetBoard();
                                }
                            }else {
                                player1_turn = !player1_turn;
                            }
                        }
                    }
                });
            }
        }


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1_wins = 0;
                player2_wins = 0;
                resetBoard();
            }
        });
    }

    public boolean checkForWin() {
        for(i = 0; i < 3; i++) {
            if(blocks[0][i].getText().toString().equals(blocks[1][i].getText().toString()) &&
                    blocks[0][i].getText().toString().equals(blocks[2][i].getText().toString()) &&
                    !blocks[0][i].getText().toString().equals("")) {
                return true;
            }
        }
        for(i = 0; i < 3; i++) {
            if(blocks[i][0].getText().toString().equals(blocks[i][1].getText().toString()) &&
                    blocks[i][0].getText().toString().equals(blocks[i][2].getText().toString()) &&
                    !blocks[i][0].getText().toString().equals("")) {
                return true;
            }
        }

        if(blocks[0][0].getText().toString().equals(blocks[1][1].getText().toString()) &&
                blocks[0][0].getText().toString().equals(blocks[2][2].getText().toString()) &&
                !blocks[0][0].getText().toString().equals("")) {
            return true;
        }

        if(blocks[0][2].getText().toString().equals(blocks[1][1].getText().toString()) &&
                blocks[0][2].getText().toString().equals(blocks[2][0].getText().toString()) &&
                !blocks[2][0].getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    public void resetBoard() {
        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
                blocks[i][j].setText("");
            }
        }
        player1_turn = true;
        tv_player1.setText("Player 1:  " + player1_wins);
        tv_player2.setText("Player 2:  " + player2_wins);
    }

    //Utility function for toast
    public void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
