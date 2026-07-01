package hk.edu.polyu.comp.comp2021.clevis.model;

public class GroupCommand implements Command {
    private Clevis clevis;
    private String command;
    private String groupName;

    public GroupCommand(Clevis clevis, String command) {
        this.clevis = clevis;
        this.command = command;
        String[] parts = command.split(" ");
        if (parts.length > 1) {
            this.groupName = parts[1];
        }
    }

    @Override
    public void execute() {
        clevis.processCommandDirectly(command);
    }

    @Override
    public void undo() {
        if (groupName != null) {
            clevis.processCommandDirectly("ungroup " + groupName);
        }
    }
}