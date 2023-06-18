package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.rule.GameOfLifeRule;
import org.fffere.jcell.view.GridPane;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static final int NROWS = 25;
    static final int NCOLS = 25;
    static final int ALIVE = 0x0000FF;

    public static void main(String[] args) {
        Grid theGrid = new Grid(NROWS, NCOLS);
        int[] generations = new int[]{
                0xFFFFFF, 0xE8E8E8, 0xD1D1D1, 0xB9B9B9, 0xA2A2A2, 0x8B8B8B, 0x747474,
                0x5D5D5D, 0x464646, 0x2E2E2E,0x171717, 0x000000
        };
//        var g = new GenerationsLifeRule(3, 2, generations);
        GridEvaluator gridEvaluator = new GridEvaluator(new GameOfLifeRule(ALIVE));
        int r = NROWS/2;
        int c = NCOLS/2;
        theGrid.set(r, c, ALIVE);
        theGrid.set(r+1, c+1, ALIVE);
        theGrid.set(r+1, c+2, ALIVE);
        theGrid.set(r, c+2, ALIVE);
        theGrid.set(r-1, c+2, ALIVE);

        AtomicBoolean running = new AtomicBoolean(true);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JCells");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            var gridPane = new GridPane(theGrid, gridEvaluator);
            var timeButton = new JButton("Time");
            timeButton.addActionListener((e) -> {
                running.set(!running.get());
            });
            gridPane.add(timeButton);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (running.get()) {
                        gridEvaluator.eval(theGrid);
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