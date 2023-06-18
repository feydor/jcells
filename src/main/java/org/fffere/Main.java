package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.GameOfLifeRule;
import org.fffere.jcell.rule.GridStateRule;
import org.fffere.jcell.view.GridPane;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static final int NROWS = 100;
    static final int NCOLS = 100;
    static final int ALIVE = 0x0000FF;

    public static void main(String[] args) throws IOException {
        Grid theGrid = new Grid(NROWS, NCOLS);
        int[] generations = new int[]{
                0xFFFFFF, 0xE8E8E8, 0xD1D1D1, 0xB9B9B9, 0xA2A2A2, 0x8B8B8B, 0x747474,
                0x5D5D5D, 0x464646, 0x2E2E2E,0x171717, 0x000000
        };
        GridEvaluator gridEvaluator = new GridEvaluator(new GameOfLifeRule(ALIVE));
        int r = NROWS/2;
        int c = NCOLS/2;
        theGrid.set(r, c, ALIVE);
        theGrid.set(r+1, c+1, ALIVE);
        theGrid.set(r+1, c+2, ALIVE);
        theGrid.set(r, c+2, ALIVE);
        theGrid.set(r-1, c+2, ALIVE);

        var file = new File("./examples/spacefiller.rle");
        var rleGrid = RleParser.parse(file, NCOLS, NROWS, ALIVE, GridStateRule.DEAD);
        theGrid = rleGrid;

        AtomicBoolean running = new AtomicBoolean(true);

        Grid finalTheGrid = theGrid;
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JCells");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            var gridPane = new GridPane(finalTheGrid, gridEvaluator);
            var timeButton = new JButton("Time");
            timeButton.addActionListener((e) -> {
                running.set(!running.get());
            });
            gridPane.add(timeButton);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (running.get()) {
                        gridEvaluator.eval(finalTheGrid);
                        gridPane.repaint();
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task, 0, 70);

            frame.getContentPane().add(gridPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}