package com.example.ndennu.todolib.model;

public class Subtask implements IClonable<Subtask> {
    private int id;
    private String text;

    private Subtask(Builder builder) {
        setId(builder.id);
        setText(builder.text);
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

    @Override
    public Subtask clone() {
        return new Builder().id(this.id).text(this.text).build();
    }


    public static final class Builder {
        private int id;
        private String text;
        private int id_task;

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

        public Subtask build() {
            return new Subtask(this);
        }
    }
}
