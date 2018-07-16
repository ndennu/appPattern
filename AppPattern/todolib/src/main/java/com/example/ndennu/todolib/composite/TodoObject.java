package com.example.ndennu.todolib.composite;

public abstract class TodoObject {

    protected String text;

    public TodoObject(String text) {
        this.text = text;
    }

    public abstract void add(TodoObject todoObject);
    public abstract void remove(TodoObject todoObject);
}
