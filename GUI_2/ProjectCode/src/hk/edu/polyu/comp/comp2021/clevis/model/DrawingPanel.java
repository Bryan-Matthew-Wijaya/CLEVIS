package hk.edu.polyu.comp.comp2021.clevis.model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel{
    private List<Shape> shapes = new ArrayList<>();
    private double zoomFactor = 1.0;
    private double panX = 0;
    private double panY = 0;

    public double getZoomFactor() {
        return zoomFactor;
    }

    void addShape(Shape shape){
        removeShape(shape.getName());
        shapes.add(shape);
    }

    public void removeShape(String name) {
        shapes.removeIf(s -> s.getName().equals(name));
    }

    public void removeAllShapes() {
        shapes.clear();
    }

    public void refreshShapes(List<Shape> newShapes) {
        shapes.clear();
        shapes.addAll(newShapes);
        repaint();
    }

    Shape getShapeByName(String name) {
        return shapes.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    public void setZoom(double zoom) {
        this.zoomFactor = zoom;
        repaint();
    }
    public void pan(double dx, double dy) {
        this.panX += dx;
        this.panY += dy;
        repaint();
    }
    public void resetView() {
        this.zoomFactor = 1.0;
        this.panX = 0;
        this.panY = 0;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(panX, panY);
        g2d.scale(zoomFactor, zoomFactor);

        drawGrid(g2d);

        for (Shape shape : shapes) {
            shape.draw(g2d);
        }
        drawCoordinateInfo(g2d);
    }
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        int gridSize = 50;

        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }
    private void drawCoordinateInfo(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawString(String.format("Zoom: %.2fx | Pan: (%.1f, %.1f)", zoomFactor, panX, panY), 10, 20);
    }
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }
}

