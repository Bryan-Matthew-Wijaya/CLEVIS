package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;

public class Rectangle extends Shape{
    private double x,y,width,height;

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    Rectangle(String name, double x, double y, double width, double height){
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect((int) x, (int) y, (int) width, (int) height);
    }
    @Override
    void move(double dx, double dy){
        x += dx; y += dy;
    }
    @Override
    public boolean coversPoint(double x1,double y1){
        double left = x;
        double right = x+width;
        double top = y;
        double bottom = y+height;

        boolean nearLeft = Math.abs(x1 - left) < 0.05 && y1 >= top && y1 <= bottom;
        boolean nearRight = Math.abs(x1 - right) < 0.05 && y1 >= top && y1 <= bottom;
        boolean nearTop = Math.abs(y1 - top) < 0.05 && x1 >= left && x1 <= right;
        boolean nearBottom = Math.abs(y1 - bottom) < 0.05 && x1 >= left && x1 <= right;

        return nearLeft || nearRight || nearTop || nearBottom;


    }
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, width, height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle (%.2f, %.2f) w:%.2f h:%.2f", x, y, width, height);
    }
}
