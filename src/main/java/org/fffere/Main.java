package org.fffere;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.fffere.jcell.GridEnvironment;
import org.fffere.jcell.view.JCellsFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static final int NROWS = 100;
    static final int NCOLS = 100;
    static final int PERIOD_MS = 60;
    static final String INITIAL_PATTERN_FILE = "./examples/spacefiller.rle";

    public static void main(String[] args) throws IOException {
        var file = new File(INITIAL_PATTERN_FILE);
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
            timer.scheduleAtFixedRate(task, 0, PERIOD_MS);
        });
    }

    private static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace(System.err);
        }
//        JFrame.setDefaultLookAndFeelDecorated(true);
    }
}