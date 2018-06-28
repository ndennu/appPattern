package com.example.ndennu.todolib;

import android.util.Log;

import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PrototypeFactory {

    private static HashMap<Type, Object> prototypes = new HashMap<>();

    private static PrototypeFactory INSTANCE;

    public static PrototypeFactory getInstance() {
        if(INSTANCE == null){
            INSTANCE = new PrototypeFactory();
        }
        return INSTANCE;
    }

    private PrototypeFactory() {

        prototypes.put(Project.class, new Project.Builder().text("New project").build());
        prototypes.put(Task.class, new Task.Builder().text("New task").build());
        prototypes.put(Subtask.class, new Subtask.Builder().text("New subTask").build());
    }

    public static Object getPrototypes(Type t) {

        if (prototypes.containsKey(t))
            return prototypes.get(t);

        return null;
    }
}
