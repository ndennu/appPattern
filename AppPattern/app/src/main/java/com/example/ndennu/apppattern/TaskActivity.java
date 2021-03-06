package com.example.ndennu.apppattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.ndennu.apppattern.adapter.TaskAdapter;
import com.example.ndennu.apppattern.adapter.TodoViewHolder;
import com.example.ndennu.todolib.Observer.ConcreteObservable;
import com.example.ndennu.todolib.Observer.Observer;
import com.example.ndennu.todolib.PrototypeFactory;
import com.example.ndennu.todolib.SQLite.DatabaseAccess;
import com.example.ndennu.todolib.SQLite.DatabaseFacade;
import com.example.ndennu.todolib.SQLite.Request;
import com.example.ndennu.todolib.memento.TodoObjectStateMemory;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends AppCompatActivity implements Observer<Task>,
        TodoItemTouchHelper.TodoItemTouchHelperListener {

    public static String ID_PROJECT = "ID_PROJECT";
    public static int ITEM_REQUEST_CODE = 1449;

    @BindView(R.id.recycler_task)
    RecyclerView recyclerTask;

    private TaskAdapter taskAdapter;
    private Project projectParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        recyclerTask.setLayoutManager(new LinearLayoutManager(this));
        initList(getIntent().getIntExtra(ID_PROJECT, 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ITEM_REQUEST_CODE) {
            initList(projectParent.getId());
        }
    }

    @Override
    public void update(Task task, Request request) {
        if (Request.UPDATE == request) {
            for (int i = 0; i < projectParent.getTasks().size(); i++) {
                if (projectParent.getTasks().get(i).getId() == task.getId())
                    projectParent.getTasks().get(i).setText(task.getText());
            }
        }

        if (Request.INSERT == request) {
            projectParent.add(task);
            taskAdapter.notifyDataSetChanged();
            openPopup(task);
        }

        if (Request.DELETE == request)
            projectParent.remove(task);


        ConcreteObservable.getINSTANCE().removeObserver(TaskActivity.this);
        taskAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.add_task)
    public void addTask() {
        Task t = (Task) new PrototypeFactory().getPrototypes(Task.class);
        if (t == null) {
            Log.e("INSERT_TASK", "ERROR INSERT TASK");
        }
        ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);
        DatabaseAccess.getInstance(TaskActivity.this).insertTask(projectParent.getId(), t);
    }

    private void initList(int idProject) {
        fetchAllTaskFromProject(idProject);
        setTitle(projectParent.getText());
        taskAdapter = new TaskAdapter(projectParent.getTasks());
        setOnClickItem();
        setOnClickImg();
        recyclerTask.setAdapter(taskAdapter);
    }

    private void fetchAllTaskFromProject(int idProject) {
        projectParent = DatabaseAccess.getInstance(TaskActivity.this).getProjectById(idProject);
    }

    private void setOnClickItem() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new TodoItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerTask);

        taskAdapter.setListener(new TaskAdapter.Listener() {
            @Override
            public void onClick(Task task) {
                Intent intent = new Intent(TaskActivity.this, SubtaskActivity.class);
                intent.putExtra(SubtaskActivity.ID_TASK, task.getId());
                startActivityForResult(intent, ITEM_REQUEST_CODE);
            }
        });
    }

    private void setOnClickImg() {
        taskAdapter.setEditListener(new TaskAdapter.EditListener() {
            @Override
            public void onEdit(final Task task) {
                openPopup(task);
            }
        });
    }

    private void openPopup(final Task task) {
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
                        task.setText(((EditText) v.findViewById(R.id.edit_text)).getText().toString());

                        ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);
                        DatabaseAccess.getInstance(TaskActivity.this).updateTask(task);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.retoreMemento(memory.getMemento());
                    }
                });
        alert.setView(v);
        alert.show();
    }

    @Override
    public void onSwiped(TodoViewHolder viewHolder, int direction, final int position) {
        new AlertDialog.Builder(TaskActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sur want to delete ?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConcreteObservable.getINSTANCE().addObserver(TaskActivity.this);
                        if (DatabaseFacade.deleteTask(TaskActivity.this, taskAdapter.getTaskList().get(position), projectParent.getId())) {
                            ConcreteObservable.getINSTANCE().notifyObservers(taskAdapter.getTaskList().get(position), Request.DELETE);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
    }
}
