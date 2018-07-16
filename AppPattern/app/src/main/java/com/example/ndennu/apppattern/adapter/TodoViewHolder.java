package com.example.ndennu.apppattern.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class TodoViewHolder extends RecyclerView.ViewHolder {
    public View card_view;

    public TodoViewHolder(View itemView) {
        super(itemView);
    }
}
