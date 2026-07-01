package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.List;

public class UngroupCommand implements Command {
    private Clevis clevis;
    private String groupName;
    private Group deletedGroup;

    public UngroupCommand(Clevis clevis, String groupName) {
        this.clevis = clevis;
        this.groupName = groupName;
    }

    @Override
    public void execute() {
        Shape shape = clevis.findShapeInAllMaps(groupName);
        if (shape instanceof Group) {
            deletedGroup = (Group) shape;
        }
        clevis.processCommandDirectly("ungroup " + groupName);
    }

    @Override
    public void undo() {
        if (deletedGroup != null) {
            clevis.addShapeDirectly(deletedGroup);
        }
    }
}