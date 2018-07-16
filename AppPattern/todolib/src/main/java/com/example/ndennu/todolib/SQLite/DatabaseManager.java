package com.example.ndennu.todolib.SQLite;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    List<Command> commands;

    public DatabaseManager() {
        this.commands = new ArrayList<>();
    }

    public void AddCommand(Command command){
        commands.add(command);
    }

    public boolean ExecuteAll() {
        int index = 0;

        try {
            for (index = 0; index < commands.size(); index++) {
                commands.get(index).Execute();
            }

            commands.clear();

            return true;

        } catch (Exception ex) {

            for (index--; index >= 0; index--) {
                commands.get(index).Cancel();
            }

            commands.clear();

            return false;
        }
    }
}
