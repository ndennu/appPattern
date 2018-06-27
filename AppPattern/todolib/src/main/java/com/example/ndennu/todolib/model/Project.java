package com.example.ndennu.todolib.model;

import java.util.List;

public class Project {
    private int id;
    private String text;
    private List<Task> tasks;

    private Project(Builder builder) {
        setId(builder.id);
        setText(builder.text);
        tasks = builder.tasks;
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
