package com.example.ndennu.apppattern;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ndennu.apppattern.adapter.ProjectAdapter;
import com.example.ndennu.apppattern.adapter.TaskAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Observer {

    @BindView(R.id.recycler_project) RecyclerView recyclerProject;

    List<Project> projects;
    ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerProject.setLayoutManager(new LinearLayoutManager(this));

        fetchAllProject();
        projectAdapter = new ProjectAdapter(projects);
        setOnClickItem();

        recyclerProject.setAdapter(projectAdapter);
    }

    @OnClick(R.id.add_project) public void addProject() {
        Project p = (Project) PrototypeFactory.getInstance().getPrototypes(Project.class);
        if (p == null) {
            Log.e("INSERT_PROJECT", "ERROR INSERT PROJECT");
        }
        ConcreteObservable.getINSTANCE().addObserver(MainActivity.this);

        Log.d("INSERT_PROJECT", "INSERT PROJECT");
        DatabaseAccess.getInstance(MainActivity.this).insertProject(p);
    }

    @Override
    public void update(int id) {
        Log.d("INSERT_PROJECT", "INSERT DONE");
        projects.add(DatabaseAccess.getInstance(MainActivity.this).getProjectById(id));
        projectAdapter.notifyDataSetChanged();
        ConcreteObservable.getINSTANCE().removeObsever(MainActivity.this);
    }

    private void fetchAllProject() {
        Log.d("FETCH_PROJECT", "FETCH PROJECT");
        projects = DatabaseAccess.getInstance(MainActivity.this).getAllProjects();
    }

    private void setOnClickItem() {
        projectAdapter.setListener(new ProjectAdapter.Listener() {
            @Override
            public void onGenreClick(Project project) {
                Log.d("MAIN_ACTIVITY", "PUSH TASK ACTIVITY");
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });
    }
}
