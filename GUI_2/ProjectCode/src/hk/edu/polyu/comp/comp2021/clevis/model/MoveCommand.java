package hk.edu.polyu.comp.comp2021.clevis.model;

public class MoveCommand implements Command {
    private Clevis clevis;
    private String shapeName;
    private double dx, dy;
    private double originalX, originalY;

    public MoveCommand(Clevis clevis, String shapeName, double dx, double dy) {
        this.clevis = clevis;
        this.shapeName = shapeName;
        this.dx = dx;
        this.dy = dy;

        Shape shape = clevis.findShapeInAllMaps(shapeName);
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            originalX = rect.getX();
            originalY = rect.getY();
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            originalX = circle.getX();
            originalY = circle.getY();
        } else if (shape instanceof Line) {
            Line line = (Line) shape;
            originalX = line.getX1();
            originalY = line.getY1();
        } else if (shape instanceof Square) {
            Square square = (Square) shape;
            originalX = square.getX();
            originalY = square.getY();
        }
    }
    @Override
    public void execute() {
        clevis.processCommand("move " + shapeName + " " + dx + " " + dy);
    }
    @Override
    public void undo() {
        // Move back to original position
        Shape shape = clevis.findShapeInAllMaps(shapeName);
        if (shape != null) {
            if (shape instanceof Rectangle) {
                Rectangle rect = (Rectangle) shape;
                double currentX = rect.getX();
                double currentY = rect.getY();
                double reverseDx = originalX - currentX;
                double reverseDy = originalY - currentY;
                clevis.processCommandDirectly("move " + shapeName + " " + reverseDx + " " + reverseDy);
            }
            // Add similar logic for other shape types...
            else {
                // Default behavior: move by negative amount
                clevis.processCommandDirectly("move " + shapeName + " " + (-dx) + " " + (-dy));
            }
        }
    }
}