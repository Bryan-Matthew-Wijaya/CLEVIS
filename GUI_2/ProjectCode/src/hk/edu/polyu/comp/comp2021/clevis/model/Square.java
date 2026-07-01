package hk.edu.polyu.comp.comp2021.clevis.model;

import java.awt.*;

public class Square extends Shape {
    private double x,y,side;

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSide() { return side; }

    Square(String name, double x, double y, double side){
        this.name = name;
        this.x = x;
        this.y = y;
        this.side = side;
    }

    @Override
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.MAGENTA);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect((int) x, (int) y, (int) side, (int) side);
    }
    @Override
    void move(double dx, double dy){
        x += dx; y += dy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean coversPoint(double x1,double y1){
        double left = x;
        double right = x+side;
        double top = y;
        double bottom = y+side;

        boolean nearLeft = Math.abs(x1 - left) < 0.05 && y1 >= top && y1 <= bottom;
        boolean nearRight = Math.abs(x1 - right) < 0.05 && y1 >= top && y1 <= bottom;
        boolean nearTop = Math.abs(y1 - top) < 0.05 && x1 >= left && x1 <= right;
        boolean nearBottom = Math.abs(y1 - bottom) < 0.05 && x1 >= left && x1 <= right;
        return nearLeft || nearRight || nearTop || nearBottom;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, side, side);
    }

    @Override
    public String toString() {
        return String.format("Square (%.2f, %.2f) side:%.2f", x, y, side);
    }

}
