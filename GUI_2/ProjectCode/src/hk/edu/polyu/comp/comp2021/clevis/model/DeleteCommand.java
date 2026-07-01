package hk.edu.polyu.comp.comp2021.clevis.model;

public class DeleteCommand implements Command {
    private Clevis clevis;
    private String shapeName;
    private Shape deletedShape;

    public DeleteCommand(Clevis clevis, String shapeName) {
        this.clevis = clevis;
        this.shapeName = shapeName;
    }

    @Override
    public void execute() {
        deletedShape = clevis.findShapeInAllMaps(shapeName);
        clevis.processCommandDirectly("delete " + shapeName);
    }

    @Override
    public void undo() {
        if (deletedShape != null) {
            clevis.addShapeDirectly(deletedShape);
        }
    }
}