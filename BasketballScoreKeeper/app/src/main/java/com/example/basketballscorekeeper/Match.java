package com.example.basketballscorekeeper;

public class Match {
    private String Team1;
    private String Team2;
    private String Score1;
    private String Score2;
    private String Winner;

    public Match(String team1, String team2, String score1, String score2, String winner) {
        Team1 = team1;
        Team2 = team2;
        Score1 = score1;
        Score2 = score2;
        Winner = winner;
    }

    public String getTeam1() {
        return Team1;
    }

    public void setTeam1(String team1) {
        Team1 = team1;
    }

    public String getTeam2() {
        return Team2;
    }

    public void setTeam2(String team2) {
        Team2 = team2;
    }

    public String getScore1() {
        return Score1;
    }

    public void setScore1(String score1) {
        Score1 = score1;
    }

    public String getScore2() {
        return Score2;
    }

    public void setScore2(String score2) {
        Score2 = score2;
    }

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String winner) {
        Winner = winner;
    }
}
