package com.example.ndennu.apppattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.ndennu.apppattern.adapter.ProjectAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.memento.TodoObjectStateMemory;
import com.example.ndennu.todolib.model.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Observer<Project> {

    @BindView(R.id.recycler_project)
    RecyclerView recyclerProject;

    private List<Project> projects;
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerProject.setLayoutManager(new LinearLayoutManager(this));

        fetchAllProject();
        projectAdapter = new ProjectAdapter(projects);
        setOnClickItem();
        setOnClickImg();

        recyclerProject.setAdapter(projectAdapter);
    }

    @OnClick(R.id.add_project)
    public void addProject() {
        Project p = (Project) new PrototypeFactory().getPrototypes(Project.class);
        if (p == null) {
            Log.e("INSERT_PROJECT", "ERROR INSERT PROJECT");
        }
        ConcreteObservable.getINSTANCE().addObserver(MainActivity.this);
        DatabaseAccess.getInstance(MainActivity.this).insertProject(p);
    }

    @Override
    public void update(Project project) {
        Log.i("azer", ""+project.getId());
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getId() == project.getId()) {
                projects.get(i).setText(project.getText());
                projectAdapter.notifyDataSetChanged();
                ConcreteObservable.getINSTANCE().removeObsever(MainActivity.this);
                Log.i("azertyuio", "sdfghjkl");
                return;
            }
        }
        projects.add(project);
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
                Log.d("MAIN_ACTIVITY", "PUSH TASK ACTIVITY ID_PROJECT: " + project.getId());
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra(TaskActivity.ID_PROJECT, project.getId());
                startActivity(intent);
            }
        });
    }

    private void setOnClickImg() {
        projectAdapter.setEditListener(new ProjectAdapter.EditListener() {
            @Override
            public void onImageClick(final Project project) {
                final TodoObjectStateMemory memory = new TodoObjectStateMemory();
                memory.setMemento(project.getMemento());

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.popup_edit, null);
                ((EditText) v.findViewById(R.id.edit_text)).setText(project.getText());

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Edit")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("DIAL", "OK");
                                project.setText(((EditText) v.findViewById(R.id.edit_text)).getText().toString());

                                ConcreteObservable.getINSTANCE().addObserver(MainActivity.this);
                                DatabaseAccess.getInstance(MainActivity.this).updateProject(project);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("DIAL", "CANCEL");
                                project.retoreMemento(memory.getMemento());
                            }
                        });
                alert.setView(v);
                alert.show();
            }
        });
    }
}
