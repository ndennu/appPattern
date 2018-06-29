package com.example.ndennu.todolib.Observer;

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

    public void notifyObservers(Project project) {
        for (Observer obs: observers) {
            obs.update(project);
        }
    }

    public void notifyObservers(Task task) {
        for (Observer obs: observers) {
            obs.update(task);
        }
    }

    public void notifyObservers(Subtask subtask) {
        for (Observer obs: observers) {
            obs.update(subtask);
        }
    }
}
