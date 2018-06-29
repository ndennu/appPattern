package com.example.ndennu.apppattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import butterknife.ButterKnife;

public class SubtaskActivity extends AppCompatActivity implements Observer<Subtask> {

    public static String ID_TASK = "ID_TASK";

    private Task taskParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask);
        ButterKnife.bind(this);

        int idTask = getIntent().getIntExtra(ID_TASK, 1);

        fetchAllSubtask(idTask);
    }

    public void addSubtask() {
        Subtask st = (Subtask) new PrototypeFactory().getPrototypes(Subtask.class);
        if (st == null) {
            Log.e("INSERT_SUBTASK", "ERROR INSERT SUBTASK");
        }
        ConcreteObservable.getINSTANCE().addObserver(SubtaskActivity.this);
        DatabaseAccess.getInstance(SubtaskActivity.this).insertSubTask(taskParent.getId(), st);
    }

    @Override
    public void update(Subtask subtask) {
        for (int i = 0; i < taskParent.getSubtasks().size(); i++) {
            if (taskParent.getSubtasks().get(i).getId() == subtask.getId()) {
                taskParent.getSubtasks().get(i).setText(subtask.getText());
//                taskAdapter.notifyDataSetChanged();
                ConcreteObservable.getINSTANCE().removeObsever(SubtaskActivity.this);
                return;
            }
        }

        taskParent.add(subtask);
//        taskAdapter.notifyDataSetChanged();

        ConcreteObservable.getINSTANCE().removeObsever(SubtaskActivity.this);
    }

    public void fetchAllSubtask(int idTask) {
        taskParent = DatabaseAccess.getInstance(SubtaskActivity.this).getTaskById(idTask);
    }
}
