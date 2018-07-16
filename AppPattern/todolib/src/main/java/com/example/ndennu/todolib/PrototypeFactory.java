package com.example.ndennu.todolib;

import com.example.ndennu.todolib.model.Project;
import com.example.ndennu.todolib.model.Subtask;
import com.example.ndennu.todolib.model.Task;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PrototypeFactory {

    private static HashMap<Type, Object> prototypes = new HashMap<>();

    public PrototypeFactory() {

        prototypes.put(Project.class, new Project.Builder().text("New project").build());
        prototypes.put(Task.class, new Task.Builder().text("New task").build());
        prototypes.put(Subtask.class, new Subtask.Builder().text("New subTask").build());
    }

    public Object getPrototypes(Type t) {

        if (prototypes.containsKey(t))
            return prototypes.get(t);

        return null;
    }
}
