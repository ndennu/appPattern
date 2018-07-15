package com.example.ndennu.apppattern.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndennu.apppattern.R;
import com.example.ndennu.todolib.model.Subtask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ViewHolder> {

    private List<Subtask> subtasks;
    private EditListener editListener;

    public SubtaskAdapter(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Subtask subtask = this.subtasks.get(position);

        holder.titleTxt.setText(subtask.getText());
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editListener != null) editListener.onEdit(subtask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.subtasks.size();
    }

    public class ViewHolder extends TodoViewHolder {

        @BindView(R.id.title)
        TextView titleTxt;
        @BindView(R.id.img_edit)
        ImageView editImg;
        @BindView(R.id.card_view)
        public View card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            super.card_view = card_view;
        }
    }

    public interface EditListener {
        void onEdit(Subtask subtask);
    }
}
