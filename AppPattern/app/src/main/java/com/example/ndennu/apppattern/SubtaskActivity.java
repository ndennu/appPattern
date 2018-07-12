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
import android.widget.TextView;

import com.example.ndennu.apppattern.adapter.SubtaskAdapter;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.SQLite.Request;
import com.example.ndennu.todolib.memento.TodoObjectStateMemory;
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

    @BindView(R.id.title_project)
    TextView projectTxt;
    @BindView(R.id.title_task)
    TextView taskTxt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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
        setOnClickImg();
        setOnClickTrash();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void update(Subtask subtask, Request request) {
        if (Request.UPDATE == request) {
            for (int i = 0; i < taskParent.getSubtasks().size(); i++) {
                if (taskParent.getSubtasks().get(i).getId() == subtask.getId()) {
                    taskParent.getSubtasks().get(i).setText(subtask.getText());
                    adapter.notifyDataSetChanged();
                    ConcreteObservable.getINSTANCE().removeObserver(SubtaskActivity.this);
                    return;
                }
            }
        }

        if (Request.INSERT == request)
            taskParent.add(subtask);

        if (Request.DELETE == request)
            taskParent.remove(subtask);

        adapter.notifyDataSetChanged();
        ConcreteObservable.getINSTANCE().removeObserver(SubtaskActivity.this);
    }

    public void fetchAllSubtask(int idTask, int projectId) {
        projectParent = DatabaseAccess.getInstance(SubtaskActivity.this).getProjectById(projectId, false);
        taskParent = DatabaseAccess.getInstance(SubtaskActivity.this).getTaskById(idTask);
    }

    // TODO: @POICET implement memento & facade pour delete



    private void setOnClickImg() {
        adapter.setEditListener(new SubtaskAdapter.EditListener() {

            @Override
            public void onImageClick(final Subtask subtask) {
                final TodoObjectStateMemory memory = new TodoObjectStateMemory();
                memory.setMemento(subtask.getMemento());
                LayoutInflater inflater = SubtaskActivity.this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.popup_edit, null);
                ((EditText) v.findViewById(R.id.edit_text)).setText(subtask.getText());

                AlertDialog.Builder alert = new AlertDialog.Builder(SubtaskActivity.this)
                        .setTitle("Edit")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                subtask.setText(((EditText) v.findViewById(R.id.edit_text)).getText().toString());

                                ConcreteObservable.getINSTANCE().addObserver(SubtaskActivity.this);
                                DatabaseAccess.getInstance(SubtaskActivity.this).updateSubtask(subtask);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                subtask.restoreMemento(memory.getMemento());
                            }
                        });
                alert.setView(v);
                alert.show();
            }
        });
    }

    private void setOnClickTrash() {

        adapter.setDeleteListener(new SubtaskAdapter.DeleteListener() {
            @Override
            public void onImageTrashClick(final Subtask subtask) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SubtaskActivity.this)
                        .setTitle("Delete")
                        .setView(R.layout.popup_delete)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ConcreteObservable.getINSTANCE().addObserver(SubtaskActivity.this);
                                DatabaseAccess.getInstance(SubtaskActivity.this).deleteSubtask(subtask.getId());
                                ConcreteObservable.getINSTANCE().notifyObservers(subtask, Request.DELETE);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                alert.show();
            }
        });
    }

}
