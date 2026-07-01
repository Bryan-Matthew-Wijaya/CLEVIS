package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Stack;

public class CommandHistory {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }
    public boolean undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return true;
        }
        return false;
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}