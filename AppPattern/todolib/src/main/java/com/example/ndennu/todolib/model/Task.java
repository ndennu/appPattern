package com.example.ndennu.todolib.model;

import com.example.ndennu.todolib.composite.TodoObject;
import com.example.ndennu.todolib.iterator.IIterator;
import com.example.ndennu.todolib.memento.TodoObjectMemento;
import com.example.ndennu.todolib.memento.TodoObjectState;

import java.util.List;

public class Task extends TodoObject implements IClonable<Task> {
    private int id;
    private String text;
    private List<Subtask> subtasks;
    private TodoObjectState state = TodoObjectState.NEW;

    private Task(Builder builder) {
        super(builder.text);
        setId(builder.id);
        setText(builder.text);
        setSubtasks(builder.subtasks);
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
        this.state = TodoObjectState.CHANGED;
        this.text = text;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }
    public void setSubtasks(List<Subtask> subtasks) {
        this.state = TodoObjectState.CHANGED;
        this.subtasks = subtasks;
    }

    public TodoObjectState getState() {
        return state;
    }
    public void setState(TodoObjectState state) {
        this.state = state;
    }

    @Override
    public Task clone() {
        return new Builder().id(this.id).text(this.text).build();
    }

    @Override
    public void add(TodoObject todoObject) {
        
    }

    @Override
    public void remove(TodoObject todoObject) {

    }

    @Override
    public void display(int depth) {

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

    public TodoObjectMemento getMemento() {
        return new TodoObjectMemento(text, state);
    }

    public void retoreMemento(TodoObjectMemento memento) {
        this.text = memento.getText();
        this.state = memento.getState();
    }

    public IIterator getIterator() {
        return new SubtaskIterator();
    }

    private class SubtaskIterator implements IIterator {

        private int index;

        @Override
        public boolean hasNext() {
            return index < subtasks.size();
        }

        @Override
        public Subtask next() {
            if (this.hasNext())
                return subtasks.get(index++);
            return null;
        }
    }
}
