package com.example.ndennu.apppattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ndennu.apppattern.adapter.ProjectAdapter;
import com.example.ndennu.apppattern.adapter.TaskAdapter;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity {

    @BindView(R.id.recycler_task) RecyclerView recyclerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        recyclerTask.setLayoutManager(new LinearLayoutManager(this));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());

        TaskAdapter projectAdapter = new TaskAdapter(tasks);

        projectAdapter.setListener(new TaskAdapter.Listener() {
            @Override
            public void onGenreClick(Task task) {
                Log.i("azeyuiop", "qsdfhklm");
            }
        });
        recyclerTask.setAdapter(projectAdapter);
    }
}
