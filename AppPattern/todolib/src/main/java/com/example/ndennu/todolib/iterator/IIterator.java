package com.example.ndennu.todolib.iterator;

import com.example.ndennu.todolib.composite.TodoObject;

public interface IIterator {
    boolean hasNext();
    TodoObject next();
}
