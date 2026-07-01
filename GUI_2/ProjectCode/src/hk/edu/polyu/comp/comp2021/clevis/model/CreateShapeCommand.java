package hk.edu.polyu.comp.comp2021.clevis.model;

public class CreateShapeCommand implements Command {
    private Clevis clevis;
    private String command;
    private Shape createdShape;

    public CreateShapeCommand(Clevis clevis, String command) {
        this.clevis = clevis;
        this.command = command;
    }

    @Override
    public void execute() {
        String[] parts = command.split(" ");
        String shapeType = parts[0];
        String name = parts[1];

        clevis.processCommand(command);

        createdShape = clevis.findShapeInAllMaps(name);
    }
    @Override
    public void undo() {
        if (createdShape != null) {
            clevis.processCommand("delete " + createdShape.getName());
        }
    }
}