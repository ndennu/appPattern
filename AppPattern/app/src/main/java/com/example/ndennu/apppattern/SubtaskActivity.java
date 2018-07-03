package com.example.ndennu.apppattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.ndennu.apppattern.adapter.SubtaskAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.SQLite.Request;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubtaskActivity extends AppCompatActivity implements Observer<Subtask> {

    private static final String ID_PROJECT = "ID_PROJECT";
    public static String ID_TASK = "ID_TASK";

    private Task taskParent;
    private Project projectParent;

    private SubtaskAdapter adapter;

    @BindView(R.id.title_project) TextView projectTxt;
    @BindView(R.id.title_task) TextView taskTxt;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask);
        ButterKnife.bind(this);

        int idTask = getIntent().getIntExtra(ID_TASK, 1);
        int idProject = getIntent().getIntExtra(ID_PROJECT, 1);

        fetchAllSubtask(idTask, idProject);
        initUI();
        setTitle(taskParent.getText());
    }

    @OnClick(R.id.add_subtask)
    public void addSubtask() {
        Subtask st = (Subtask) new PrototypeFactory().getPrototypes(Subtask.class);
        if (st == null) {
            Log.e("INSERT_SUBTASK", "ERROR INSERT SUBTASK");
        }
        ConcreteObservable.getINSTANCE().addObserver(SubtaskActivity.this);
        DatabaseAccess.getInstance(SubtaskActivity.this).insertSubTask(taskParent.getId(), st);
    }

    private void initUI() {
        projectTxt.setText(projectParent.getText());
        taskTxt.setText(taskParent.getText());

        adapter = new SubtaskAdapter(taskParent.getSubtasks());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void update(Subtask subtask, Request request) {
        if (Request.UPDATE == request) {
            for (int i = 0; i < taskParent.getSubtasks().size(); i++) {
                if (taskParent.getSubtasks().get(i).getId() == subtask.getId()) {
                    taskParent.getSubtasks().get(i).setText(subtask.getText());
//                taskAdapter.notifyDataSetChanged();
                    ConcreteObservable.getINSTANCE().removeObsever(SubtaskActivity.this);
                    return;
                }
            }
        }

        if (Request.INSERT == request)
            taskParent.add(subtask);

        if (Request.DELETE == request)
            taskParent.remove(subtask);
//        taskAdapter.notifyDataSetChanged();

        ConcreteObservable.getINSTANCE().removeObsever(SubtaskActivity.this);
    }

    public void fetchAllSubtask(int idTask, int projectId) {
        projectParent = DatabaseAccess.getInstance(SubtaskActivity.this).getProjectById(projectId, false);
        taskParent = DatabaseAccess.getInstance(SubtaskActivity.this).getTaskById(idTask);
    }
}
