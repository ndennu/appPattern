package com.example.ndennu.todolib.memento;

public class TodoObjectMemento {

    private String text;
    private TodoObjectState state;

    public TodoObjectMemento(String text, TodoObjectState state) {
        this.text = text;
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TodoObjectState getState() {
        return state;
    }

    public void setState(TodoObjectState state) {
        this.state = state;
    }
}
