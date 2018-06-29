package com.example.ndennu.todolib.Observer;

import com.example.ndennu.todolib.SQLite.Request;
import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    protected List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObsever(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Project project, Request request) {
        for (Observer obs: observers) {
            obs.update(project, request);
        }
    }

    public void notifyObservers(Task task, Request request) {
        for (Observer obs: observers) {
            obs.update(task, request);
        }
    }

    public void notifyObservers(Subtask subtask, Request request) {
        for (Observer obs: observers) {
            obs.update(subtask, request);
        }
    }
}
