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
    private DeleteListener deleteListener;

    public SubtaskAdapter(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
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
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener != null) deleteListener.onImageTrashClick(subtask);
            }
        });
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editListener != null) editListener.onImageClick(subtask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.subtasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView titleTxt;
        @BindView(R.id.img_edit)
        ImageView editImg;
        @BindView(R.id.img_delete)
        ImageView deleteImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface EditListener {
        void onImageClick(Subtask subtask);
    }

    public interface DeleteListener {
        void onImageTrashClick(Subtask subtask);
    }
}
