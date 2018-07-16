package com.example.ndennu.todolib.Observer;

import com.example.ndennu.todolib.SQLite.Request;

public interface Observer<T> {
    void update(T todoObject, Request request);
}
