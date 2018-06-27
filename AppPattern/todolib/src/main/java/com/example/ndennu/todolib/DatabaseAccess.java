package com.example.ndennu.todolib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static DatabaseAccess instance;

    private static final String DATABASE_NAME = "com.nicoberangerenico.todos.sqlite";
    private int DATABASE_VERSION = 1;

    private MySQLiteOpenHelper mySQLiteOpenHelper;


    private DatabaseAccess(Context context){
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DatabaseAccess(context);
        }

        return instance;
    }


    /**
     * Insert Project
     * @param project Project to insert
     */
    public void insertProject(Project project){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO project VALUES (" +
                project.getId() + ", \"" +
                project.getText() + "\")");
        db.close();
    }


    /**
     * Insert task
     * @param project_id Project id
     * @param task Task to insert
     */
    public void insertTask(int project_id, Task task){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO task VALUES (" +
                task.getId() + ", " +
                project_id + ", \"" +
                task.getText() + "\")");
        db.close();
    }


    /**
     * Insert subtask
     * @param task_id  Task id
     * @param subtask Subtask to insert
     */
    public void insertSubTask(int task_id, Subtask subtask){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO subtask VALUES (" +
                subtask.getId() + ", " +
                task_id + ", \"" +
                subtask.getText() + "\")");
        db.close();
    }


    /**
     * delete a project from database
     * @param project_id Project to delete
     */
    public void deleteProject(long project_id){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM project WHERE id = " + project_id);
        db.close();
    }


    /**
     * delete a task from database
     * @param task_id Task to delete
     */
    public void deleteTask(long task_id){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM task WHERE id = " + task_id);
        db.close();
    }


    /**
     * delete a subtask from database
     * @param subtask_id Subtask to delete
     */
    public void deleteSubtask(int subtask_id){
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM subtask WHERE id = " + subtask_id);
        db.close();
    }


    /**
     * Get all projects
     * @return List of projects
     */
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM project ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            projects.add(
                    new Project.Builder()
                            .id(cursor.getInt(0))
                            .text(cursor.getString(1))
                            .build()
            );

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return projects;

    }

    /**
     * Get tasks for a project
     * @param project_id Project id
     * @return List of Tasks
     */
    public List<Task> getTasks(int project_id) {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM subtask WHERE task_id = " + project_id + " ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            tasks.add(
                    new Task.Builder()
                            .id(cursor.getInt(0))
                            .text(cursor.getString(1))
                            .build()
            );

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return tasks;
    }


    /**
     * Get subtasks for a task
     * @param task_id Task id
     * @return List of Subtasks
     */
    public List<Subtask> getSubtasks(int task_id) {
        List<Subtask> subtasks = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM subtask WHERE task_id = " + task_id + " ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            subtasks.add(
                    new Subtask.Builder()
                            .id(cursor.getInt(0))
                            .text(cursor.getString(1))
                            .build()
            );

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return subtasks;
    }


    /*public void updateSubtask(Subtask subtask) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        db.rawQuery("UPDATE subtask SET text = " +  + " ", null);
    }*/
}
