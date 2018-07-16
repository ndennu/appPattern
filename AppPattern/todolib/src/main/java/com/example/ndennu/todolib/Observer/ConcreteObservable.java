package com.example.ndennu.todolib.Observer;

public class ConcreteObservable extends Observable {

    private static ConcreteObservable INSTANCE;

    public static ConcreteObservable getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ConcreteObservable();
        return INSTANCE;
    }

    private ConcreteObservable() {
    }
}
