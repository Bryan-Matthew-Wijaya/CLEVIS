package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;

public abstract class Shape {
    String name;
    abstract void draw(Graphics g);
    abstract void move(double dx, double dy);
    abstract String getName();
    abstract BoundingBox getBoundingBox();
    abstract boolean coversPoint(double x, double y);
}
