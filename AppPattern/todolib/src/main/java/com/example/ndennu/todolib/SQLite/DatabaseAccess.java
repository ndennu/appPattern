package com.example.ndennu.todolib.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.R;
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


    private DatabaseAccess(Context context) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }

        return instance;
    }


    /**
     * Insert Project
     * @param project project Project to insert
     * @return id
     */
    public int insertProject(Project project) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO project(text) VALUES (\"" +
                project.getText() + "\")");

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();

        int id = cursor.getInt(0);

        cursor.close();
        db.close();

        project.setId(id);

        ConcreteObservable.getINSTANCE().notifyObservers(project, Request.INSERT);

        return id;
    }

    /**
     * Insert task
     *
     * @param project_id Project id
     * @param task       Task to insert
     * @return id
     */
    public int insertTask(int project_id, Task task) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO task(project_id, text) VALUES (" +
                project_id + ", \"" +
                task.getText() + "\")");

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();

        int id = cursor.getInt(0);

        cursor.close();
        db.close();

        task.setId(id);

        ConcreteObservable.getINSTANCE().notifyObservers(task, Request.INSERT);

        return id;
    }


    /**
     * Insert subtask
     *
     * @param task_id Task id
     * @param subtask Subtask to insert
     * @return id
     */
    public int insertSubTask(int task_id, Subtask subtask) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("INSERT INTO subtask(task_id, text) VALUES (" +
                task_id + ", \"" +
                subtask.getText() + "\")");

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();

        int id = cursor.getInt(0);

        cursor.close();
        db.close();

        subtask.setId(id);
        ConcreteObservable.getINSTANCE().notifyObservers(subtask, Request.INSERT);

        return id;
    }


    /**
     * delete a project from database
     *
     * @param project_id Project to delete
     */
    public void deleteProject(int project_id) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM project WHERE id = " + project_id);
        db.close();
    }


    /**
     * delete a task from database
     *
     * @param task_id Task to delete
     */
    public void deleteTask(int task_id) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM task WHERE id = " + task_id);
        db.close();
    }


    /**
     * delete a subtask from database
     *
     * @param subtask_id Subtask to delete
     */
    public void deleteSubtask(int subtask_id) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("DELETE FROM subtask WHERE id = " + subtask_id);
        db.close();
    }


    /**
     * Get all projects
     *
     * @return List of projects
     */
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM project ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
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

        for (Project project : projects) {
            project.setTasks(getTasks(project.getId()));
        }

        return projects;

    }

    /**
     * Get tasks for a project
     *
     * @param project_id Project id
     * @return List of Tasks
     */
    public List<Task> getTasks(int project_id) {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM task WHERE project_id = " + project_id + " ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
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

        for (Task task : tasks) {
            task.setSubtasks(getSubtasks(task.getId()));
        }

        return tasks;
    }


    /**
     * Get subtasks for a task
     *
     * @param task_id Task id
     * @return List of Subtasks
     */
    public List<Subtask> getSubtasks(int task_id) {
        List<Subtask> subtasks = new ArrayList<>();

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM subtask WHERE task_id = " + task_id + " ORDER BY id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
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

    public Project getProjectById(int project_id) {
        return getProjectById(project_id, true);
    }

    /**
     * Get project by id
     *
     * @param project_id Project id to get
     * @return Project
     */
    public Project getProjectById(int project_id, boolean withTasks) {
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM project WHERE id = " + project_id, null);
        cursor.moveToFirst();

        Project project = new Project.Builder()
                .id(cursor.getInt(0))
                .text(cursor.getString(1))
                .build();

        cursor.close();
        db.close();

        if(withTasks){
            project.setTasks(getTasks(project_id));
        }

        return project;
    }


    /**
     * Get task by id
     *
     * @param task_id Task id to get
     * @return Task
     */
    public Task getTaskById(int task_id) {
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM task WHERE id = " + task_id, null);
        cursor.moveToFirst();

        Task task = new Task.Builder()
                .id(cursor.getInt(0))
                .text(cursor.getString(1))
                .build();

        cursor.close();
        db.close();

        task.setSubtasks(getSubtasks(task_id));

        return task;
    }


    /**
     * Get subtask by id
     *
     * @param subtask_id Subtask id to get
     * @return Subtask
     */
    public Subtask getSubtaskById(int subtask_id) {
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, text FROM subtask WHERE id = " + subtask_id, null);
        cursor.moveToFirst();

        Subtask subtask = new Subtask.Builder()
                .id(cursor.getInt(0))
                .text(cursor.getString(1))
                .build();

        cursor.close();
        db.close();

        return subtask;
    }


    /**
     * Update a project
     *
     * @param project Project to update
     */
    public void updateProject(Project project) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("UPDATE project SET text = \"" + project.getText() + "\" WHERE id = " + project.getId());
        db.close();
        ConcreteObservable.getINSTANCE().notifyObservers(project, Request.UPDATE);
    }


    /**
     * Update a task
     *
     * @param task Task to update
     */
    public void updateTask(Task task) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("UPDATE task SET text = \"" + task.getText() + "\" WHERE id = " + task.getId());
        db.close();
        ConcreteObservable.getINSTANCE().notifyObservers(task, Request.UPDATE);
    }


    /**
     * Update a subtask
     *
     * @param subtask Subtask to update
     */
    public void updateSubtask(Subtask subtask) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL("UPDATE subtask SET text = \"" + subtask.getText() + "\" WHERE id = " + subtask.getId());
        db.close();
        ConcreteObservable.getINSTANCE().notifyObservers(subtask, Request.UPDATE);
    }
}
