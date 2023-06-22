package org.fffere;

import org.fffere.jcell.GridEnvironment;
import org.fffere.jcell.view.JCellsFrame;

import javax.swing.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static final int NROWS = 100;
    static final int NCOLS = 100;
    static final int INTERVAL = 60;

    public static void main(String[] args) {
        var file = new File("./examples/spacefiller.rle");
        var gridEnvironment = new GridEnvironment(file, NROWS, NCOLS);

        initLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            var frame = new JCellsFrame(gridEnvironment);
            var task = new TimerTask() {
                @Override
                public void run() {
                    if (gridEnvironment.isRunning()) {
                        gridEnvironment.run();
                        frame.repaint();
                    }
                }
            };
            var timer = new Timer();
            timer.scheduleAtFixedRate(task, 0, INTERVAL);
        });
    }

    private static void initLookAndFeel() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}