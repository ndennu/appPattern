package com.example.ndennu.todolib.SQLite.Project;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Command;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Project;

public class UpdateProject implements Command {

    private Project project;
    private Project oldProject;
    private Context context;

    public UpdateProject(Project project, Context context) {
        this.project = project;
        this.context = context;
    }

    @Override
    public void Execute() {
        oldProject = DatabaseAccess.getInstance(context).getProjectById(project.getId());
        DatabaseAccess.getInstance(context).updateProject(project);
    }

    @Override
    public void Cancel() {
        DatabaseAccess.getInstance(context).updateProject(oldProject);
    }
}
