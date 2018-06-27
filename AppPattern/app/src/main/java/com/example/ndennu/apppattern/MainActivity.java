package com.example.ndennu.apppattern;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_project) RecyclerView recyclerProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerProject.setLayoutManager(new LinearLayoutManager(this));

        List<Project> projects = new ArrayList<>();
        projects.add(new Project.Builder().id(1).text("First project").build());
        projects.add(new Project.Builder().id(2).text("Second project").build());

        ProjectAdapter projectAdapter = new ProjectAdapter(projects);

        projectAdapter.setListener(new ProjectAdapter.Listener() {
            @Override
            public void onGenreClick(Project project) {
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });
        recyclerProject.setAdapter(projectAdapter);
    }
}
