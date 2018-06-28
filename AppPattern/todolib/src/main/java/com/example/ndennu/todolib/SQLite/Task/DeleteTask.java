package com.example.ndennu.todolib.SQLite.Task;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Task;

public class DeleteTask implements Command{

    private Task task;
    private int project_id;
    private Context context;

    public DeleteTask(Context context, Task task, int project_id) {
        this.task = task;
        this.context = context;
        this.project_id = project_id;
    }

    @Override
    public void Execute() {
        DatabaseAccess.getInstance(context).deleteTask(task.getId());
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).insertTask(project_id, task);
    }
}
