package com.example.ndennu.apppattern;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ndennu.apppattern.adapter.TaskAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends AppCompatActivity implements Observer<Task> {

    public static String ID_PROJECT = "ID_PROJECT";

    @BindView(R.id.recycler_task)
    RecyclerView recyclerTask;

    private List<Task> tasks;
    private TaskAdapter taskAdapter;
    int idProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        recyclerTask.setLayoutManager(new LinearLayoutManager(this));

        idProject = getIntent().getIntExtra(ID_PROJECT, 1);
        Log.d("TASK_ACTIVITY", "ID_PROJECT: " + idProject);

        fetchAllTaskFromProject();

        taskAdapter = new TaskAdapter(tasks);
        setOnClickItem();

        recyclerTask.setAdapter(taskAdapter);
    }

    @OnClick(R.id.add_task)
    public void addTask() {
        Task t = (Task) PrototypeFactory.getInstance().getPrototypes(Task.class);
        if (t == null) {
            Log.e("INSERT_TASK", "ERROR INSERT TASK");
        }
        ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);

        Log.d("INSERT_TASK", "INSERT TASK");
        DatabaseAccess.getInstance(TaskActivity.this).insertTask(idProject, t);
    }

    @Override
    public void update(Task task) {
        Log.d("INSERT_TASK", "TASK DONE");
        tasks.add(DatabaseAccess.getInstance(TaskActivity.this).getTaskById(task.getId()));
        taskAdapter.notifyDataSetChanged();
        ConcreteObservable.getINSTANCE().removeObsever(TaskActivity.this);
    }

    private void fetchAllTaskFromProject() {
        Log.d("FETCH_TASK", "FETCH TASK");
        tasks = DatabaseAccess.getInstance(TaskActivity.this).getTasks(idProject);
    }

    private void setOnClickItem() {
        taskAdapter.setListener(new TaskAdapter.Listener() {
            @Override
            public void onGenreClick(Task task) {
                Log.d("TASK_ACTIVITY", "PUSH ??? ACTIVITY");
                /*Intent intent = new Intent(TaskActivity.this, TaskActivity.class);
                intent.putExtra(TaskActivity.ID_PROJECT, project.getId());
                startActivity(intent);*/
            }
        });
    }
}
