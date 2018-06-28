package com.example.ndennu.todolib.SQLite.Project;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Project;

public class DeleteProject implements Command {

    private Project project;
    private Context context;

    public DeleteProject(Context context, Project project) {
        this.project = project;
        this.context = context;
    }

    @Override
    public void Execute() {
        DatabaseAccess.getInstance(context).deleteProject(project.getId());
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).insertProject(project);
    }
}