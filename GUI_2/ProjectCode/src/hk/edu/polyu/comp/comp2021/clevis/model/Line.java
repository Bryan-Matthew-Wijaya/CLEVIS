package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;

public class Line extends Shape {
    private double x1,y1,x2,y2;

    public double getX1() { return x1; }
    public double getY1() { return y1; }
    public double getX2() { return x2; }
    public double getY2() { return y2; }

    Line(String name, double x1, double y1, double x2, double y2){
        this.name = name;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }
    @Override
    void move(double dx, double dy){
        x1 += dx; y1 += dy; x2 += dx; y2 += dy;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public boolean coversPoint(double x, double y){
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lineLength = Math.sqrt(dx * dx + dy * dy);

        if (lineLength < 0.0001) {
            double distToP1 = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
            double distToP2 = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
            return Math.min(distToP1, distToP2) < 0.05;
        }

        double t = ((x - x1) * dx + (y - y1) * dy) / (lineLength * lineLength);
        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        double distance = Math.sqrt(Math.pow(x - closestX, 2) + Math.pow(y - closestY, 2));

        return distance < 0.05;
    }

    @Override
    public BoundingBox getBoundingBox() {
        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double maxX = Math.max(x1, x2);
        double maxY = Math.max(y1, y2);
        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public String toString() {
        return String.format("Line (%.2f, %.2f) to (%.2f, %.2f)", x1, y1, x2, y2);
    }
}
