package com.example.ndennu.todolib.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    protected List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObsever(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(int id) {
        for (Observer obs: observers) {
            obs.update(id);
        }
    }
}
