package com.example.ndennu.todolib.model;

import java.util.List;

public class Project implements IClonable<Project> {
    private int id;
    private String text;
    private List<Task> tasks;

    private Project(Builder builder) {
        setId(builder.id);
        setText(builder.text);
        setTasks(builder.tasks);
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

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Project clone() {

        return new Builder().id(this.id).text(this.text).build();
    }


    public static final class Builder {
        private int id;
        private String text;
        private List<Task> tasks;

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

        public Builder tasks(List<Task> val) {
            tasks = val;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }
}
