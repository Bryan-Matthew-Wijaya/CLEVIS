package hk.edu.polyu.comp.comp2021.clevis.model;

public interface Command {
    void execute();
    void undo();
}