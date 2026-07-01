package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;

public class Circle extends Shape {
    private double x,y,radius;

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

    Circle(String name, double x, double y, double radius){
        this.name = name;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }
    @Override
    void move(double dx, double dy){
        x += dx; y += dy;
    }
    @Override
    public boolean coversPoint(double x1,double y1){
        double centerDistance = Math.sqrt(Math.pow(x1-x,2) + Math.pow(y1-y,2));
        return Math.abs(centerDistance - radius) < 0.05;
    }
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public String toString() {
        return String.format("Circle (%.2f, %.2f) r:%.2f", x, y, radius);
    }
}
