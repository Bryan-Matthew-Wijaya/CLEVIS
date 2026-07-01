package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.model.Clevis;
import hk.edu.polyu.comp.comp2021.clevis.model.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Application {
    private static DrawingPanel drawingPanel;
    private static JFrame frame;
    private static Clevis clevis;
    private static JTextField commandField;
    private static JTextArea outputArea;
    private static JButton executeButton;
    private static JButton undoButton;
    private static JButton redoButton;
    private static JButton zoomInButton;
    private static JButton zoomOutButton;
    private static JButton resetZoomButton;

    public static void main(String[] args) {
        clevis = new Clevis(args);

        boolean guiMode = true;
        for (String arg : args) {
            if ("-nogui".equals(arg)) {
                guiMode = false;
                break;
            }
        }
        if (guiMode) {
            SwingUtilities.invokeLater(() -> initializeGUI());
        }

        handleCLI();
    }
    private static void initializeGUI() {
        frame = new JFrame("Clevis - Vector Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel();
        clevis.setDrawingPanel(drawingPanel);

        JPanel commandPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        commandField = new JTextField();
        executeButton = new JButton("Execute");

        inputPanel.add(new JLabel("Command: "), BorderLayout.WEST);
        inputPanel.add(commandField, BorderLayout.CENTER);
        inputPanel.add(executeButton, BorderLayout.EAST);

        JPanel controlPanel = new JPanel(new FlowLayout());
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        zoomInButton = new JButton("Zoom In");
        zoomOutButton = new JButton("Zoom Out");
        resetZoomButton = new JButton("Reset Zoom");

        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        controlPanel.add(zoomInButton);
        controlPanel.add(zoomOutButton);
        controlPanel.add(resetZoomButton);

        outputArea = new JTextArea(8, 50);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        commandPanel.add(inputPanel, BorderLayout.NORTH);
        commandPanel.add(controlPanel, BorderLayout.CENTER);
        commandPanel.add(outputScroll, BorderLayout.SOUTH);

        executeButton.addActionListener(new ExecuteCommandListener());
        commandField.addActionListener(new ExecuteCommandListener());
        undoButton.addActionListener(e -> {
            if (clevis.undo()) {
                outputArea.append("Undo successful\n");
                drawingPanel.repaint();
            } else {
                outputArea.append("Nothing to undo\n");
            }
        });
        redoButton.addActionListener(e -> {
            if (clevis.redo()) {
                outputArea.append("Redo successful\n");
                drawingPanel.repaint();
            } else {
                outputArea.append("Nothing to redo\n");
            }
        });

        setupZoomListeners();

        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(commandPanel, BorderLayout.SOUTH);

        frame.setSize(1000, 800);
        frame.setVisible(true);

        outputArea.append("Clevis GUI started. Enter commands below.\n");
    }

    private static void setupZoomListeners() {
        zoomInButton.addActionListener(e -> {
            drawingPanel.setZoom(drawingPanel.getZoomFactor() * 1.2);
            drawingPanel.repaint();
            outputArea.append("Zoomed in to: " + String.format("%.2f", drawingPanel.getZoomFactor()) + "x\n");
        });

        zoomOutButton.addActionListener(e -> {
            drawingPanel.setZoom(drawingPanel.getZoomFactor() / 1.2);
            drawingPanel.repaint();
            outputArea.append("Zoomed out to: " + String.format("%.2f", drawingPanel.getZoomFactor()) + "x\n");
        });

        resetZoomButton.addActionListener(e -> {
            drawingPanel.resetView();
            drawingPanel.repaint();
            outputArea.append("Zoom reset to 1.0x\n");
        });
    }

    private static class ExecuteCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = commandField.getText().trim();
            if (!command.isEmpty()) {
                outputArea.append("> " + command + "\n");
                String result = clevis.processCommandWithResult(command);
                if (result != null && !result.isEmpty()) {
                    outputArea.append(result + "\n");
                }
                commandField.setText("");
                drawingPanel.repaint();
            }
        }
    }

    private static void handleCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Clevis started. Enter commands (type 'quit' to exit):");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                break;
            }
            if (!input.isEmpty()) {
                clevis.processCommand(input);
            }
        }

        clevis.close();

        if (frame != null) frame.dispose();
        scanner.close();
        System.out.println("Clevis exiting.");

    }




}
