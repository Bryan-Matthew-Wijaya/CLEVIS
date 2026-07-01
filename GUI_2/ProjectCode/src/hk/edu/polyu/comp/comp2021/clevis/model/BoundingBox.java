package hk.edu.polyu.comp.comp2021.clevis.model;

public class BoundingBox {
    private final double x;
    private final double y;
    private final double w;
    private final double h;

    public BoundingBox(double x,double y,double w,double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    @Override
    public String toString(){
        return String.format("%.2f %.2f %.2f %.2f",x,y,w,h);
    }

    public static BoundingBox union(BoundingBox box1, BoundingBox box2) {
        if (box1 == null) return box2;
        if (box2 == null) return box1;

        double minX = Math.min(box1.x, box2.x);
        double minY = Math.min(box1.y, box2.y);
        double maxX = Math.max(box1.x + box1.w, box2.x + box2.w);
        double maxY = Math.max(box1.y + box1.h, box2.y + box2.h);

        return new BoundingBox(minX,minY,maxX-minX,maxY-minY);
    }

}
