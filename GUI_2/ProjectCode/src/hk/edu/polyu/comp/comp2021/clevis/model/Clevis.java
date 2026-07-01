package hk.edu.polyu.comp.comp2021.clevis.model;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;

//import javax.swing.Group;

public class Clevis {
    private CommandHistory commandHistory = new CommandHistory();
    private Logger logger;
    private Map<String,Shape> shapes = new LinkedHashMap<>();

    private static DrawingPanel drawingPanel;

    public Map<String, Rectangle> rectangleMap = new HashMap<>();
    public Map<String, Line> lineMap = new HashMap<>();
    public Map<String, Circle> circleMap = new HashMap<>();
    public Map<String, Square> squareMap = new HashMap<>();
    public Map<String, Group> groupMap = new HashMap<>();

    public Clevis(String[] args){
        String htmlPath = HtmlPath(args);
        String txtPath = TxtPath(args);
        this.logger = new Logger(htmlPath, txtPath);
        this.shapes = new HashMap<>();
    }

    public void debugShapeExistence(String name) {
        System.out.println("DEBUG - Checking shape: " + name);
        System.out.println("  rectangleMap contains: " + rectangleMap.containsKey(name));
        System.out.println("  lineMap contains: " + lineMap.containsKey(name));
        System.out.println("  circleMap contains: " + circleMap.containsKey(name));
        System.out.println("  squareMap contains: " + squareMap.containsKey(name));
        System.out.println("  groupMap contains: " + groupMap.containsKey(name));
        System.out.println("  shapes contains: " + shapes.containsKey(name));
    }

    public void refreshDrawingPanel() {
        if (drawingPanel != null) {
            drawingPanel.removeAllShapes();

            for (Rectangle rect : rectangleMap.values()) {
                drawingPanel.addShape(rect);
            }
            for (Line line : lineMap.values()) {
                drawingPanel.addShape(line);
            }
            for (Circle circle : circleMap.values()) {
                drawingPanel.addShape(circle);
            }
            for (Square square : squareMap.values()) {
                drawingPanel.addShape(square);
            }
            for (Group group : groupMap.values()) {
                drawingPanel.addShape(group);
            }
            drawingPanel.repaint();
        }
    }

    public String processCommandWithResult(String command) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream oldOut = System.out;
        System.setOut(ps);

        try {
            processCommand(command);

            if (isUndoableCommand(command)) {
                Command cmd = createCommandObject(command);
                if (cmd != null) {
                    commandHistory.executeCommand(cmd);
                }
            }
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }
        return baos.toString();
    }

    public void processCommandDirectly(String command) {
        processCommand(command);
    }

    public boolean undo() {
        boolean success = commandHistory.undo();
        if (success && drawingPanel != null) {
            refreshDrawingPanel();
            drawingPanel.repaint();
        }
        return success;
    }

    public boolean redo() {
        boolean success = commandHistory.redo();
        if (success && drawingPanel != null) {
            refreshDrawingPanel();
            drawingPanel.repaint();
        }
        return success;
    }

    private boolean isUndoableCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) return false;

        String operation = parts[0];
        String[] nonUndoable = {"boundingbox", "intersect", "list", "listAll", "quit", "shapeAt"};

        for (String cmd : nonUndoable) {
            if (cmd.equals(operation)) {
                return false;
            }
        }
        return true;
    }

    private Command createCommandObject(String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) return null;

        String operation = parts[0];

        switch (operation) {
            case "rectangle":
            case "line":
            case "circle":
            case "square":
                return new CreateShapeCommand(this, command);
            case "move":
                if (parts.length >= 4) {
                    String name = parts[1];
                    double dx = Double.parseDouble(parts[2]);
                    double dy = Double.parseDouble(parts[3]);
                    return new MoveCommand(this, name, dx, dy);
                }
                break;
            case "delete":
                if (parts.length >= 2) {
                    String name = parts[1];
                    return new DeleteCommand(this, name);
                }
                break;
            case "group":
                if (parts.length >= 3) {
                    return new GroupCommand(this, command);
                }
                break;
            case "ungroup":
                if (parts.length >= 2) {
                    String name = parts[1];
                    return new UngroupCommand(this, name);
                }
                break;
        }
        return null;
    }

    public void setDrawingPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    private String HtmlPath(String[] args){
        for(int i = 0; i<args.length-1; i++){
            if("-html".equals(args[i])){
                return args[i+1];
            }
        }
        return "default.html";
    }
    private String TxtPath(String[] args){
        for(int i = 0; i<args.length-1; i++){
            if("-txt".equals(args[i])){
                return args[i+1];
            }
        }
        return "default.txt";
    }

    private void removeShapeFromMaps(String name) {
        rectangleMap.remove(name);
        lineMap.remove(name);
        circleMap.remove(name);
        squareMap.remove(name);
        groupMap.remove(name);
        shapes.remove(name);

        if (drawingPanel != null) {
            drawingPanel.removeShape(name);
        }
    }

    public void addShapeDirectly(Shape shape) {
        if (shape == null) return;

        String name = shape.getName();
        if (rectangleMap.containsKey(name)) rectangleMap.remove(name);
        if (lineMap.containsKey(name)) lineMap.remove(name);
        if (circleMap.containsKey(name)) circleMap.remove(name);
        if (squareMap.containsKey(name)) squareMap.remove(name);
        if (groupMap.containsKey(name)) groupMap.remove(name);

        if (shape instanceof Rectangle) {
            rectangleMap.put(name, (Rectangle) shape);
        } else if (shape instanceof Line) {
            lineMap.put(name, (Line) shape);
        } else if (shape instanceof Circle) {
            circleMap.put(name, (Circle) shape);
        } else if (shape instanceof Square) {
            squareMap.put(name, (Square) shape);
        } else if (shape instanceof Group) {
            groupMap.put(name, (Group) shape);
        }

        if (drawingPanel != null) {
            drawingPanel.removeShape(name); // Remove if exists
            drawingPanel.addShape(shape);
            drawingPanel.repaint();
        }
    }
    public void processCommand(String command){
        if(command == null || command.trim().isEmpty()){
            return;
        }
        String[] parts = command.split(" ");
        String operation = parts[0];
        try {
            switch (operation) {
                case "rectangle":
                    handleRectangle(parts);
                    break;
                case "line":
                    handleLine(parts);
                    break;
                case "circle":
                    handleCircle(parts);
                    break;
                case "square":
                    handleSquare(parts);
                    break;
                case "group":
                    handleGroup(parts);
                    break;
                case "ungroup":
                    handleUngroup(parts);
                    break;
                case "delete":
                    handleDelete(parts);
                    break;
                case "move":
                    handleMove(parts);
                    break;
                case "boundingbox":
                    if (parts.length == 2) {
                        String n = parts[1];
                        handleBoundingBox(n);
                    } else {
                        System.out.println("Shape not found");
                    }
                    break;
                case "shapeAt":
                    if (parts.length == 3) {
                        try {
                            double x = Double.parseDouble(parts[1]);
                            double y = Double.parseDouble(parts[2]);
                            handleShapeAt(x, y);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid coordinates");
                        }
                    }
                    break;
                case "intersect":
                    if (parts.length == 3) {
                        String shape1 = parts[1];
                        String shape2 = parts[2];
                        handleIntersect(shape1, shape2);
                    } else {
                        System.out.println("Intersect requires 2 shapes names");
                    }
                    break;
                case "list":
                    handleList(parts);
                    break;
                case "listAll":
                    handleListAll();
                    break;
                default:
                    return;
            }
        }catch (Exception e){
            System.out.println("Error executing command: " + e.getMessage());
        }
            logger.logCommand(command);
    }

    public void close() {
        if (logger != null) {
            logger.close();
        }
    }

    private void handleRectangle(String[] p){
        if (p.length != 6){ System.out.println("Usage: rectangle n x y w h"); return; }
        try{
            String name = p[1];

            if (findShapeInAllMaps(name) != null) {
                System.out.println("Rectangle with name " + name + " already exists.");
                return;
            }

            double x = Double.parseDouble(p[2]), y = Double.parseDouble(p[3]);
            double w = Double.parseDouble(p[4]), h = Double.parseDouble(p[5]);

            Rectangle rect = new Rectangle(name, x, y, w, h);
            rectangleMap.put(name, rect);
            shapes.put(name, rect);

            if (drawingPanel != null) {
                drawingPanel.addShape(rect);
                drawingPanel.repaint();
            }

            System.out.printf("Rectangle %s created at (%.2f, %.2f) with width %.2f and height %.2f.%n", name, x, y, w, h);
        } catch (NumberFormatException e){
            System.out.println("Invalid numbers in rectangle command.");
        }
    }

    private void handleLine(String[] p){
        if (p.length != 6){ System.out.println("Usage: line n x1 y1 x2 y2"); return; }
        try{
            String name = p[1];
            double x1 = Double.parseDouble(p[2]), y1 = Double.parseDouble(p[3]);
            double x2 = Double.parseDouble(p[4]), y2 = Double.parseDouble(p[5]);

            if (lineMap.containsKey(name)){
                System.out.println("Line with name " + name + " already exists.");
                return;
            }
            Line line = new Line(name, x1, y1, x2, y2);
            lineMap.put(name, line);
            shapes.put(name, line);
            drawingPanel.addShape(line);
            drawingPanel.repaint();
            System.out.printf("Line %s created from (%.2f, %.2f) to (%.2f, %.2f).%n",
                    name, x1, y1, x2, y2);
        } catch (NumberFormatException e){
            System.out.println("Invalid numbers in line command.");
        }
    }

    private void handleCircle(String[] p){
        if (p.length != 5){ System.out.println("Usage: circle n x y r"); return; }
        try{
            String name = p[1];
            double x = Double.parseDouble(p[2]), y = Double.parseDouble(p[3]);
            double r = Double.parseDouble(p[4]);

            if (circleMap.containsKey(name)){
                System.out.println("Circle with name " + name + " already exists.");
                return;
            }
            Circle circle = new Circle(name, x, y, r);
            circleMap.put(name, circle);
            shapes.put(name, circle);
            drawingPanel.addShape(circle);
            drawingPanel.repaint();
            System.out.printf("Circle %s created at (%.2f, %.2f) with radius %.2f.%n",
                    name, x, y, r);
        } catch (NumberFormatException e){
            System.out.println("Invalid numbers in circle command.");
        }
    }

    private void handleSquare(String[] p){
        if (p.length != 5){ System.out.println("Usage: square n x y l"); return; }
        try{
            String name = p[1];
            double x = Double.parseDouble(p[2]), y = Double.parseDouble(p[3]);
            double l = Double.parseDouble(p[4]);

            if (squareMap.containsKey(name)){
                System.out.println("Square with name " + name + " already exists.");
                return;
            }
            Square square = new Square(name, x, y, l);
            squareMap.put(name, square);
            shapes.put(name, square);
            drawingPanel.addShape(square);
            drawingPanel.repaint();
            System.out.printf("Square %s created at (%.2f, %.2f) with side length %.2f.%n",
                    name, x, y, l);
        } catch (NumberFormatException e){
            System.out.println("Invalid numbers in square command.");
        }
    }

    private void handleGroup(String[] p){
        if(p.length<3){
            System.out.println("Error: Group command requires at least a group name and one shape.");
            return;
        }
        String groupName=p[1];
        if (findShapeInAllMaps(groupName) != null) {
            System.out.println("Error: Shape with name " + groupName + " already exists.");
            return;
        }
        Group group=new Group(groupName);

        for(int i = 2; i < p.length; i++) {
            String shapeName = p[i];
            Shape shape = findShapeInAllMaps(shapeName);
            if(shape == null) {
                System.out.println("Error: shape " + shapeName + " does not exist.");
                return;
            }
            group.add(shape);
        }
        for(int i = 2; i < p.length; i++) {
            String shapeName = p[i];
            removeShapeFromMaps(shapeName);
        }
        shapes.put(groupName, group);
        groupMap.put(groupName, group);

        if (drawingPanel != null) {
            drawingPanel.addShape(group);
            drawingPanel.repaint();
        }
        System.out.println("Group " + groupName + " created.");
        if (drawingPanel != null) {
            refreshDrawingPanel();
            drawingPanel.repaint();
        }
    }

    private void handleUngroup(String[] p){
        if(p.length!=2){
            System.out.println("Error: Ungroup command requires exectly one group name.");
            return;
        }
        String groupName=p[1];
        Shape shape=shapes.get(groupName);
        if(!(shape instanceof Group)){
            System.out.println("Error: "+groupName+" is not a group.");
            return;
        }
        Group group=(Group) shape;
        shapes.remove(groupName);
        groupMap.remove(groupName);
        for(Shape s:group.getShapes()) {
            String shapeName = s.getName();
            shapes.put(shapeName, s);
            if(s instanceof Rectangle){
                rectangleMap.put(shapeName, (Rectangle) s);
            }else if (s instanceof Circle) {
                circleMap.put(shapeName, (Circle) s);
            }else if (s instanceof Line) {
                lineMap.put(shapeName, (Line) s);
            }else if (s instanceof Square) {
                squareMap.put(shapeName, (Square) s);
            }else if (s instanceof Group) {
                groupMap.put(shapeName, (Group) s);
            }

        }
        if (drawingPanel != null) {
            drawingPanel.removeShape(groupName);
            // Add the individual shapes back to drawing panel
            for(Shape s : group.getShapes()) {
                drawingPanel.addShape(s);
            }
            drawingPanel.repaint();
        }
        System.out.println("Group "+groupName+" ungrouped.");
    }

    private void handleDelete(String[] p){
        if (p.length != 2){
            System.out.println("Usage: delete n");
            return;
        }
        String name = p[1];

        boolean removed = false;

        if (rectangleMap.containsKey(name)){
            rectangleMap.remove(name);
            removed = true;
        } else if (lineMap.containsKey(name)){
            lineMap.remove(name);
            removed = true;
        } else if (circleMap.containsKey(name)){
            circleMap.remove(name);
            removed = true;
        } else if (squareMap.containsKey(name)){
            squareMap.remove(name);
            removed = true;
        }

        if (removed){
            drawingPanel.removeShape(name);
            drawingPanel.repaint();
            System.out.println("Shape " + name + " deleted.");
        } else{
            System.out.println("No shape named " + name + " exists.");
        }

        if (drawingPanel != null) {
            refreshDrawingPanel();
            drawingPanel.repaint();
        }
    }

    private void handleMove(String[] p){
        if (p.length != 4){
            System.out.println("Usage: move n dx dy");
            return;
        }
        String name = p[1];
        try{
            double dx = Double.parseDouble(p[2]);
            double dy = Double.parseDouble(p[3]);

            Shape shape = drawingPanel.getShapeByName(name);

            if (shape == null){
                System.out.println("No shape named " + name + " exists.");
                return;
            }

            shape.move(dx, dy);
            drawingPanel.repaint();

            System.out.printf("Shape %s moved by (%.2f, %.2f).%n", name, dx, dy);

        } catch (NumberFormatException e){
            System.out.println("Invalid dx or dy values.");
        }
    }

    public Shape findShapeInAllMaps(String name) {
        // Search through all maps
        if (rectangleMap.containsKey(name)) return rectangleMap.get(name);
        if (lineMap.containsKey(name)) return lineMap.get(name);
        if (circleMap.containsKey(name)) return circleMap.get(name);
        if (squareMap.containsKey(name)) return squareMap.get(name);
        if (groupMap.containsKey(name)) return groupMap.get(name);
        return null;
    }

    private List<Shape> getAllShapesFromAllMaps() {
        List<Shape> allShapes = new ArrayList<>();
        allShapes.addAll(rectangleMap.values());
        allShapes.addAll(lineMap.values());
        allShapes.addAll(circleMap.values());
        allShapes.addAll(squareMap.values());
        allShapes.addAll(groupMap.values());
        return allShapes;
    }

    private void handleBoundingBox(String n){
        Shape shape = findShapeInAllMaps(n);
        if(shape == null){
            System.out.println("Error:Shape " + n + " is not found. ");
            return;
        }
        BoundingBox box = shape.getBoundingBox();
        System.out.println(box.toString());

    }

    private List<Shape> getAllShapesInZOrder() {
        List<Shape> allShapes = new ArrayList<>();
        allShapes.addAll(shapes.values());

        return allShapes;
    }

    private void handleShapeAt(double x, double y){
        List<Shape> allShapes = getAllShapesInZOrder();

        for (int i = allShapes.size() - 1; i >= 0; i--) {
            Shape shape = allShapes.get(i);
            if (shape.coversPoint(x, y)) {
                System.out.println(shape.getName());
                return;
            }
        }
        System.out.println("No shape cover at point (" + x + ", " + y + ")");
    }

    private void handleIntersect(String n1, String n2){
        Shape shape1 = shapes.get(n1);
        Shape shape2 = shapes.get(n2);

        if(shape1 == null){
            System.out.println("Shape" + n1 + "is null");
            return;
        }
        if(shape2 == null){
            System.out.println("Shape " + n2+ " is null");
            return;
        }

        BoundingBox box1 = shape1.getBoundingBox();
        BoundingBox box2 = shape2.getBoundingBox();
        boolean isIntersect = intersects(box1,box2);
        System.out.println(isIntersect);
    }

    private boolean intersects(BoundingBox box1, BoundingBox box2) {
        if (box1 == null || box2 == null) return false;

        double box1Right = box1.getX() + box1.getW();
        double box1Bottom = box1.getY() + box1.getH();
        double box2Right = box2.getX() + box2.getW();
        double box2Bottom = box2.getY() + box2.getH();

        boolean xOverlap = (box1.getX() < box2.getX() + box2.getW()) && (box1.getX() + box1.getW() > box2.getX());
        boolean yOverlap = (box1.getY() < box2.getY() + box2.getH()) && (box1.getY() + box1.getH() > box2.getY());
        return xOverlap && yOverlap;
    }
    private void handleList(String[] p){
        if(p.length!=2){
            System.out.println("Error: List command requires exactly one shape name.");
            return;
        }
        String shapeName=p[1];
        Shape shape=shapes.get(shapeName);
        if(shape==null){
            System.out.println("Error: Shape "+shapeName+" does not exsist.");
            return;
        }
        System.out.println(shape.toString());
    }

    private void handleListAll(){
        for(Shape shape:shapes.values())if(!isChildShape(shape))printShapeInfo(shape,0);
    }

    private void printShapeInfo(Shape shape,int indent){
        String indentStr="  ".repeat(indent);
        System.out.println(indentStr + shape.getName() + ": " + shape.toString());
        if (shape instanceof Group) {
            Group g = (Group) shape;
            for (Shape child : g.getShapes()) {
                printShapeInfo(child, indent + 1); // Fixed syntax: indent + 1
            }
        }
}

    private boolean isChildShape(Shape shape) {
        for (Shape s : shapes.values()) {
            if (s instanceof Group && ((Group) s).containsShape(shape)) {
                return true;
            }
        }
        return false;
    }
}
