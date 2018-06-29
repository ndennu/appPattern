package com.example.ndennu.apppattern.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndennu.apppattern.R;
import com.example.ndennu.todolib.model.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private Listener listener;
    private EditListener editListener;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task t = taskList.get(position);

        holder.titleTask.setText(t.getText());

        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editListener != null) editListener.onImageClick(t);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onGenreClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_task)
        TextView titleTask;
        @BindView(R.id.img_edit_task)
        ImageView editTask;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Listener {
        void onGenreClick(Task task);
    }

    public interface EditListener {
        void onImageClick(Task task);
    }
}
