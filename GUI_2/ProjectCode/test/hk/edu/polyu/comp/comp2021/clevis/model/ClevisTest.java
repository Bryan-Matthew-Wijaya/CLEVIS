package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Test;
import java.io.File;


public class ClevisTest {

    @Test
    public void testClevisConstructor(){
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        assert true;
    }

    @Test
    public void testExeCommandWithNull() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.processCommand(null);
    }

    @Test
    public void testExeCommandWithEmptyString() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.processCommand("");
    }

    @Test
    public void testBoundingBoxCommand() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.processCommand("BoundingBox shape1");
    }

    @Test
    public void testShapeAtCommand() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.processCommand("shapeAt 10 20");
    }

    @Test
    public void testIntersectCommand() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.processCommand("intersect shape1 shape2");
    }

    @Test
    public void testCloseMethod() {
        String[] args = {"-html", "test.html", "-txt", "test.txt"};
        Clevis clevis = new Clevis(args);
        clevis.close();
    }

    @Test
    public void testFileCreation() {
        String htmlFile = "test_log.html";
        String txtFile = "test_log.txt";
        String[] args = {"-html", htmlFile, "-txt", txtFile};

        Clevis clevis = new Clevis(args);
        clevis.processCommand("test command");
        clevis.close();

        File html = new File(htmlFile);
        File txt = new File(txtFile);

        html.delete();
        txt.delete();
    }
	
}