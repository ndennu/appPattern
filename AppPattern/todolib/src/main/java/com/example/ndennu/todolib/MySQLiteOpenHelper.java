package com.example.ndennu.todolib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    // Create table project if not exist
    private String createProject =
            "CREATE TABLE IF NOT EXISTS project (" +
                    "id INTEGER PRIMARY KEY," +
                    "text TEXT);";

    // Create table task if not exist
    private String createTask =
            "CREATE TABLE IF NOT EXISTS task (" +
                    "id INTEGER PRIMARY KEY," +
                    "project_id INTEGER," +
                    "text TEXT);";

    // Create table subtask if not exist
    private String createSubTask =
            "CREATE TABLE IF NOT EXISTS subtask (" +
                    "id INTEGER PRIMARY KEY," +
                    "task_id INTEGER," +
                    "text TEXT);";


    MySQLiteOpenHelper(Context context, String database_name, int database_version) {
        super(context, database_name, null, database_version);
        onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createProject);
        db.execSQL(createTask);
        db.execSQL(createSubTask);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
