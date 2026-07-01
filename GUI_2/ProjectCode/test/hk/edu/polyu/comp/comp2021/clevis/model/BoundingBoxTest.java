package hk.edu.polyu.comp.comp2021.clevis.model;

    import org.junit.Test;

public class BoundingBoxTest {

    @Test
    public void testBoundingBoxConstructor() {
        BoundingBox box = new BoundingBox(10.0, 20.0, 30.0, 40.0);
    }

    @Test
    public void testBoundingBoxGetters() {
        BoundingBox box = new BoundingBox(10.0, 20.0, 30.0, 40.0);
        double x = box.getX();
        double y = box.getY();
        double w = box.getW();
        double h = box.getH();
    }

    @Test
    public void testBoundingBoxToString() {
        BoundingBox box = new BoundingBox(10.0, 20.0, 30.0, 40.0);
        String result = box.toString();
    }

    @Test
    public void testBoundingBoxUnion() {
        BoundingBox box1 = new BoundingBox(10, 10, 20, 20);
        BoundingBox box2 = new BoundingBox(25, 25, 20, 20);

        BoundingBox result = BoundingBox.union(box1, box2);
    }

    @Test
    public void testBoundingBoxUnionWithNull() {
        BoundingBox box1 = new BoundingBox(10, 10, 20, 20);

        BoundingBox result1 = BoundingBox.union(box1, null);
        BoundingBox result2 = BoundingBox.union(null, box1);
        BoundingBox result3 = BoundingBox.union(null, null);
    }
}
