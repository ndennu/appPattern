package com.example.ndennu.todolib.model;

import com.example.ndennu.todolib.composite.TodoObject;
import com.example.ndennu.todolib.iterator.IIterator;
import com.example.ndennu.todolib.memento.TodoObjectMemento;
import com.example.ndennu.todolib.memento.TodoObjectState;

import java.util.ArrayList;
import java.util.List;

public class Project extends TodoObject implements IClonable<Project> {
    private int id;
    private String text;
    private List<Task> tasks;
    private TodoObjectState state = TodoObjectState.NEW;

    private Project(Builder builder) {
        super(builder.text);
        setId(builder.id);
        setText(builder.text);
        setTasks(builder.tasks);
        setState(TodoObjectState.LOADED);
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
        state = TodoObjectState.CHANGED;
        this.text = text;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        state = TodoObjectState.CHANGED;
        this.tasks = tasks;
    }

    public TodoObjectState getState() {
        return state;
    }

    public void setState(TodoObjectState state) {
        this.state = state;
    }

    @Override
    public Project clone() {

        return new Builder().id(this.id).text(this.text).build();
    }

    @Override
    public void add(TodoObject todoObject) {
        tasks.add((Task) todoObject);
    }

    @Override
    public void remove(TodoObject todoObject) {
        tasks.remove(todoObject);
    }


    public static final class Builder {
        private int id;
        private String text;
        private List<Task> tasks;

        public Builder() {
            tasks = new ArrayList<>();
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

    public TodoObjectMemento getMemento() {
        return new TodoObjectMemento(text, state);
    }

    public void retoreMemento(TodoObjectMemento memento) {
        this.text = memento.getText();
        this.state = memento.getState();
    }

    public IIterator<Task> getIterator() {
        return new TaskIterator();
    }

    private class TaskIterator implements IIterator<Task> {

        private int index;

        @Override
        public boolean hasNext() {
            return index < tasks.size();
        }

        @Override
        public Task next() {
            if (this.hasNext())
                return tasks.get(index++);
            return null;
        }
    }
}
