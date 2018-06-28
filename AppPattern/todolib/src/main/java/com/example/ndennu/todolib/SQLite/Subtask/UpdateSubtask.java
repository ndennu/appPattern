package com.example.ndennu.todolib.SQLite.Subtask;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Subtask;

public class UpdateSubtask implements Command {

    private Subtask subtask;
    private Subtask oldSubtask;
    private Context context;

    public UpdateSubtask(Subtask subtask, Context context) {
        this.subtask = subtask;
        this.context = context;
    }

    @Override
    public void Execute() {
        oldSubtask = DatabaseAccess.getInstance(context).getSubtaskById(subtask.getId());
        DatabaseAccess.getInstance(context).updateSubtask(subtask);
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).updateSubtask(oldSubtask);
    }
}