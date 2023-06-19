package org.fffere.jcell.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class JCellsFrame extends JFrame {
    private final GridPane gridPanel;
    private final JButton openButton;
    private final JFileChooser fileChooser = new JFileChooser(ResourceConstants.EXAMPLES_DIR);

    public JCellsFrame(GridPane gridPane, Component[] menuItems) {
        this.gridPanel = gridPane;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JCells");

        var menuBar = new JMenuBar();
        for (var mi : menuItems)
            menuBar.add(mi);

        openButton = new JButton("Open a pattern...",
                new ImageIcon(ResourceConstants.IMG_OPEN_FILE));
        openButton.addActionListener(this::handleFileOpenButton);
        menuBar.add(openButton);
        setJMenuBar(menuBar);

        JPanel mainPane = new JPanel(new GridLayout(0, 1, 10, 10));
        int margins = 20;
        mainPane.setBorder(new EmptyBorder(margins, margins, margins, margins));
        getContentPane().add(mainPane);

        mainPane.add(gridPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleFileOpenButton(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.getSelectedFile();
                try {
                    System.out.println("Opening: " + file.getName());
                    gridPanel.loadPatternFile(file);
                } catch (IOException ex) {
                    System.out.println("Error opening file '" + file + "'");
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Open command cancelled by user");
            }
        }
    }
}
