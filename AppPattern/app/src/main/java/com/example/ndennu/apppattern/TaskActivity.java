package com.example.ndennu.apppattern;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.ndennu.apppattern.adapter.TaskAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.memento.TodoObjectStateMemory;
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

        fetchAllTaskFromProject();
        taskAdapter = new TaskAdapter(tasks);
        setOnClickItem();
        setOnClickImg();

        recyclerTask.setAdapter(taskAdapter);
    }

    @OnClick(R.id.add_task)
    public void addTask() {
        Task t = (Task) PrototypeFactory.getInstance().getPrototypes(Task.class);
        if (t == null) {
            Log.e("INSERT_TASK", "ERROR INSERT TASK");
        }
        ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);
        DatabaseAccess.getInstance(TaskActivity.this).insertTask(idProject, t);
    }

    @Override
    public void update(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.get(i).setText(task.getText());
                taskAdapter.notifyDataSetChanged();
                ConcreteObservable.getINSTANCE().removeObsever(TaskActivity.this);
                return;
            }
        }
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

    private void setOnClickImg() {
        taskAdapter.setEditListener(new TaskAdapter.EditListener() {
            @Override
            public void onImageClick(final Task task) {
                final TodoObjectStateMemory memory = new TodoObjectStateMemory();
                memory.setMemento(task.getMemento());
                LayoutInflater inflater = TaskActivity.this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.popup_edit, null);
                ((EditText) v.findViewById(R.id.edit_text)).setText(task.getText());

                AlertDialog.Builder alert = new AlertDialog.Builder(TaskActivity.this)
                        .setTitle("Edit")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("DIAL", "OK");
                                task.setText(((EditText) v.findViewById(R.id.edit_text)).getText().toString());

                                ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);
                                DatabaseAccess.getInstance(TaskActivity.this).updateTask(task);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("DIAL", "CANCEL");
                                task.retoreMemento(memory.getMemento());
                            }
                        });
                alert.setView(v);
                alert.show();
            }
        });
    }
}
