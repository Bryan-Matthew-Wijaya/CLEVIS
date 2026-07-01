package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Group extends Shape{

    List<Shape> shapes = new ArrayList<>();

    public boolean containsShape(Shape targetShape) {
        for (Shape s : shapes) {
            if (s == targetShape) {
                return true;
            }
            if (s instanceof Group) {
                Group g = (Group) s;
                if (g.containsShape(targetShape)) {
                    return true;
                }
            }
        }
        return false;
    }

    Group(String name) {
        this.name = name;
    }
    void add(Shape s) {
        shapes.add(s);
    }
    List<Shape>getShapes(){
        return shapes;
    }
    @Override
    void draw(Graphics g){
        for (Shape s : shapes) s.draw(g);
    }
    @Override
    void move(double dx, double dy){
        for (Shape s : shapes) s.move(dx, dy);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public boolean coversPoint(double x, double y) {
        for (Shape shape : shapes) {
            if (shape.coversPoint(x, y)) return true;
        }
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        if (shapes.isEmpty()) {
            return new BoundingBox(0, 0, 0, 0);
        }
        BoundingBox union = null;

        for (Shape shape : shapes) {
            BoundingBox childBox = shape.getBoundingBox();
            if (union == null) {
                union = childBox;
            } else {
                union = BoundingBox.union(union, childBox);
            }
        }
        return union;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Group ").append(name).append(" [");
        for (int i = 0; i < shapes.size(); i++) {
            if (i > 0) str.append(", ");
            str.append(shapes.get(i).getName());
        }
        str.append("]");
        return str.toString();
    }
}
