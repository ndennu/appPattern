package com.example.ndennu.apppattern.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndennu.apppattern.R;
import com.example.ndennu.todolib.model.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<Project> projectList;
    private Listener listener;
    private EditListener editListener;
    private DeleteListener deleteListener;

    public ProjectAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
                .inflate(R.layout.item_project, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Project p = projectList.get(position);

        holder.titleProject.setText(p.getText());
        holder.editProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editListener != null) editListener.onImageClick(p);
            }
        });
        holder.deleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener != null) deleteListener.onTrashClick(p);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onGenreClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_project)
        TextView titleProject;
        @BindView(R.id.img_edit)
        ImageView editProject;
        @BindView(R.id.img_delete)
        ImageView deleteProject;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Listener {
        void onGenreClick(Project project);
    }

    public interface EditListener {
        void onImageClick(Project project);
    }

    public interface DeleteListener {
        void onTrashClick(Project project);
    }
}
