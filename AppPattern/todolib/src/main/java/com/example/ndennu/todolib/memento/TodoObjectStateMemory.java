package com.example.ndennu.todolib.memento;

public class TodoObjectStateMemory {

    private TodoObjectMemento memento;

    public TodoObjectMemento getMemento() {
        return memento;
    }

    public void setMemento(TodoObjectMemento memento) {
        this.memento = memento;
    }
}
