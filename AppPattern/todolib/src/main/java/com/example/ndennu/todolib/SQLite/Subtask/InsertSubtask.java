package com.example.ndennu.todolib.SQLite.Subtask;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Subtask;

public class InsertSubtask implements Command {

    private Subtask subtask;
    private int task_id;
    private Context context;

    public InsertSubtask(Context context, Subtask subtask, int task_id) {
        this.subtask = subtask;
        this.context = context;
        this.task_id = task_id;
    }

    @Override
    public void Execute() {
        DatabaseAccess.getInstance(context).insertSubTask(task_id, subtask);
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).deleteSubtask(subtask.getId());
    }
}

