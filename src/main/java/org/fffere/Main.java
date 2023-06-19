package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.GameOfLifeRule;
import org.fffere.jcell.rule.GenerationsLifeRule;
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
        int[] generations = new int[]{
                0xFFFFFF, 0xE8E8E8, 0xD1D1D1, 0xB9B9B9, 0xA2A2A2, 0x8B8B8B, 0x747474,
                0xFFFFFF, 0xE8E8E8, 0xD1D1D1, 0xB9B9B9, 0xA2A2A2, 0x8B8B8B, 0x747474,
                0x5D5D5D, 0x464646, 0x2E2E2E,0x171717, 0x000000,
                0x5D5D5D, 0x464646, 0x2E2E2E,0x171717, 0x000000
        };

        int[] states = new int[]{
          0xBFBFBF, 0x7F7F7F, 0x3F3F3F, 0x000000
        };

        // star wars
        // B2/S345/4
        var genRule = new GenerationsLifeRule(new int[]{2}, new int[]{3, 4, 5}, states);
        var golRule = new GameOfLifeRule(ALIVE);

        GridEvaluator gridEvaluator = new GridEvaluator(golRule);
        var file = new File("./examples/spacefiller.rle");
        Grid theGrid = RleParser.parse(file, NCOLS, NROWS, ALIVE, GridStateRule.DEAD);

        initLookAndFeel();
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

            new JCellsFrame(gridPane, new JButton[]{ timeButton, tickButton });

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