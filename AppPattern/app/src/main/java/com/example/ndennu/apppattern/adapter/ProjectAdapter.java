package com.example.ndennu.apppattern.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

    public ProjectAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    public List<Project> getProjectList() {
        return projectList;
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
        final Project p = projectList.get(position);

        holder.nbTaskTxt.setText(String.format("%s task(s)", Integer.toString(p.getTasks().size())));

        holder.titleTxt.setText(p.getText());

        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editListener != null) editListener.onEdit(p);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ViewHolder extends TodoViewHolder {

        @BindView(R.id.title)
        TextView titleTxt;
        @BindView(R.id.img_edit)
        ImageView editImg;
        @BindView(R.id.number_task)
        TextView nbTaskTxt;

        @BindView(R.id.card_view)
        public View card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            super.card_view = card_view;
        }
    }

    public interface Listener {
        void onClick(Project project);
    }

    public interface EditListener {
        void onEdit(Project project);
    }
}
