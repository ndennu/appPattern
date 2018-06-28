package com.example.ndennu.todolib.SQLite.Task;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Task;

public class UpdateTask implements Command {

    private Task task;
    private Task oldTask;
    private Context context;

    public UpdateTask(Task task, Context context) {
        this.task = task;
        this.context = context;
    }

    @Override
    public void Execute() {
        oldTask = DatabaseAccess.getInstance(context).getTaskById(task.getId());
        DatabaseAccess.getInstance(context).updateTask(task);
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).updateTask(oldTask);
    }
}
