package com.example.floatingactionbutton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDB {

    Context context;
    ArrayList<Task> tasks;
    private static final String DATABASE_NAME ="Task";
    private static final String TABLE_NAME = "TaskTable";
    private static final String COL1 = "_id";
    private static final String COL2 = "_name";
    private static final String COL3 = "_des";
    private static final String COL4 = "_date";
    private static final String COL5 = "is_completed";
    private static final String COL6 = "is_overdue";
    DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public  TaskDB(Context context) {
        this.context = context;
    }

    public class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sqlQuery ="CREATE TABLE " + TABLE_NAME + " ( " +
                    COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " TEXT NOT NULL, " +
                    COL3 + " TEXT, " +
                    COL4 + " TEXT NOT NULL, " +
                    COL5 + " INTEGER, " +
                    COL6 + " INTEGER " + ")";

            sqLiteDatabase.execSQL(sqlQuery);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public TaskDB open() throws SQLException{
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public long createTask(String name, String Des, String date, int isCompleted, int isOverdue) {
        ContentValues cv = new ContentValues();
        cv.put(COL2, name);
        cv.put(COL3, Des);
        cv.put(COL4, date);
        cv.put(COL5, isCompleted);
        cv.put(COL6, isOverdue);
        return sqLiteDatabase.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<Task> getTasks() {
        tasks = new ArrayList<>();
        String[] Columns = new String[] {COL1, COL2, COL3, COL4, COL5, COL6};
        Cursor c = sqLiteDatabase.query(TABLE_NAME, Columns, null, null, null, null, null );
        while (c.moveToNext()) {
            tasks.add(new Task(c.getString(c.getColumnIndex(COL2)), c.getString(c.getColumnIndex(COL3)), c.getString(c.getColumnIndex(COL4)), c.getInt(c.getColumnIndex(COL5)), c.getInt(c.getColumnIndex(COL6))));
        }
        return tasks;
    }

    public int deleteEntry(String name) {
        return sqLiteDatabase.delete(TABLE_NAME, COL2 + "=?", new String[]{name});
    }

    public long clearAll() {
        return sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

    public long clearCompleted() {return sqLiteDatabase.delete(TABLE_NAME, COL5 + "=?", new String[]{"1"});}

    public int updateChecked(String name, int num) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL5, num);
        return sqLiteDatabase.update(TABLE_NAME, contentValues, COL2 + "=?", new String[]{name});
    }
    public int updateOverdue(String name, int num) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL6, num);
        return sqLiteDatabase.update(TABLE_NAME, contentValues, COL2 + "=?", new String[]{name});
    }

    public int updateAll(String name, String Des, String Date, int isOverdue, String oldName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, Des);
        contentValues.put(COL4, Date);
        contentValues.put(COL6, isOverdue);
        return sqLiteDatabase.update(TABLE_NAME, contentValues, COL2 + "=?", new String[]{oldName});
    }
    public void close(){
        dbHelper.close();
    }
}
