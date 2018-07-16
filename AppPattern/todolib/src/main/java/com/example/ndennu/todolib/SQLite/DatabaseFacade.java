package com.example.ndennu.todolib.SQLite;

import android.content.Context;

import com.example.ndennu.todolib.SQLite.Project.DeleteProject;
import com.example.ndennu.todolib.SQLite.Subtask.DeleteSubtask;
import com.example.ndennu.todolib.SQLite.Task.DeleteTask;
import com.example.ndennu.todolib.iterator.IIterator;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

public class DatabaseFacade {

    /**
     * Delete project
     *
     * @param context Context
     * @param project Project to delete
     * @return True if successfully deleted, else False
     */
    public static boolean deleteProject(Context context, Project project) {
        DatabaseManager databaseManager = new DatabaseManager();

        for (IIterator<Task> taskIterator = project.getIterator(); taskIterator.hasNext(); ) {
            Task task = taskIterator.next();

            for (IIterator<Subtask> subtaskIterator = task.getIterator(); subtaskIterator.hasNext(); ) {
                databaseManager.AddCommand(new DeleteSubtask(context, subtaskIterator.next(), task.getId()));
            }

            databaseManager.AddCommand(new DeleteTask(context, task, project.getId()));
        }

        databaseManager.AddCommand(new DeleteProject(context, project));

        return databaseManager.ExecuteAll();
    }


    /**
     * Delete task
     *
     * @param context    Context
     * @param task       Task to delete
     * @param project_id Project id
     * @return True if successfully deleted, else False
     */
    public static boolean deleteTask(Context context, Task task, int project_id) {
        DatabaseManager databaseManager = new DatabaseManager();

        for (IIterator<Subtask> subtaskIterator = task.getIterator(); subtaskIterator.hasNext(); ) {
            databaseManager.AddCommand(new DeleteSubtask(context, subtaskIterator.next(), task.getId()));
        }

        databaseManager.AddCommand(new DeleteTask(context, task, project_id));

        return databaseManager.ExecuteAll();
    }
}
