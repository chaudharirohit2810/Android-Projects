package com.example.basketballscorekeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MatchDB {

    Context context;
    ArrayList<Match> matches;
    private static final String COL1 = "_id";
    private static final String COL2 = "_team1";
    private static final String COL3 = "_team2";
    private static final String COL4 = "_score1";
    private static final String COL5 = "_score2";
    private static final String COL6 = "_winner";
    private static final String DATABASE_NAME = "MATCH";
    private static final String TABLE_NAME = "MatchTable";
    DBHelper dbHelper;
    private SQLiteDatabase db;

    public MatchDB(Context context) {
        this.context = context;
    }

    public class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sqlCode = "CREATE TABLE " + TABLE_NAME + " ( " +
                    COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " TEXT NOT NULL, " +
                    COL3 + " TEXT NOT NULL, " +
                    COL4 + " TEXT NOT NULL, " +
                    COL5 + " TEXT NOT NULL, " +
                    COL6 + " TEXT NOT NULL " + ")";

            sqLiteDatabase.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public MatchDB open() throws SQLException{
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long createEntry(String team1, String team2, String score1, String score2, String winner) {
        ContentValues cv = new ContentValues();
        cv.put(COL2, team1);
        cv.put(COL3, team2);
        cv.put(COL4, score1);
        cv.put(COL5, score2);
        cv.put(COL6, winner);
        return db.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<Match> getData() {
        matches = new ArrayList<>();
        String[] Columns = new String[] {COL1, COL2, COL3, COL4, COL5, COL6};
        Cursor c = db.query(TABLE_NAME, Columns, null, null, null, null, null );
        while (c.moveToNext()) {
            matches.add(new Match(c.getString(c.getColumnIndex(COL2)), c.getString(c.getColumnIndex(COL3)), c.getString(c.getColumnIndex(COL4)), c.getString(c.getColumnIndex(COL5)), c.getString(c.getColumnIndex(COL6))));
        }
        return matches;
    }

    public long clearAll() {
        return db.delete(TABLE_NAME, null, null);
    }

    public void close() {
        dbHelper.close();
    }

}
