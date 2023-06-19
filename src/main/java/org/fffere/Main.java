package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.GameOfLifeRule;
import org.fffere.jcell.rule.GridStateRule;
import org.fffere.jcell.view.GridPane;
import org.fffere.jcell.view.JCellsFrame;
import org.fffere.jcell.view.ResourceConstants;
import org.fffere.jcell.view.TickButton;

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
        GridEvaluator gridEvaluator = new GridEvaluator(new GameOfLifeRule(ALIVE));
        var file = new File("./examples/spacefiller.rle");
        Grid theGrid = RleParser.parse(file, NCOLS, NROWS, ALIVE, GridStateRule.DEAD);

        AtomicBoolean running = new AtomicBoolean(false);

        SwingUtilities.invokeLater(() -> {
            var gridPane = new GridPane(theGrid, gridEvaluator, ALIVE);
            var timeButton = new JButton("Play");
            timeButton.setIcon(new ImageIcon(ResourceConstants.IMG_PLAY));
            timeButton.addActionListener(e -> {
                running.set(!running.get());
            });
            gridPane.add(timeButton);

            TickButton tickButton = new TickButton(() -> {
                gridEvaluator.eval(gridPane.grid);
                gridPane.repaint();
                return null;
            });
            tickButton.setIcon(new ImageIcon(ResourceConstants.IMG_RIGHT_ARROW));

            var frame = new JCellsFrame(gridPane, new JButton[]{ timeButton, tickButton });

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (running.get()) {
                        gridEvaluator.eval(gridPane.grid);
                        gridPane.repaint();
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task, 0, 70);
        });
    }
}