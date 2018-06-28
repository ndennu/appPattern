package com.example.ndennu.todolib.model;

import java.util.List;

public class Task implements IClonable<Task> {
    private int id;
    private String text;
    private List<Subtask> subtasks;

    private Task(Builder builder) {
        setId(builder.id);
        setText(builder.text);
        setSubtasks(builder.subtasks);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public Task clone() {
        return new Builder().id(this.id).text(this.text).build();
    }


    public static final class Builder {
        private int id;
        private String text;
        private int id_project;
        private List<Subtask> subtasks;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder subtasks(List<Subtask> val) {
            subtasks = val;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
