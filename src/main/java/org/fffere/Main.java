package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.StateRulesDb;
import org.fffere.jcell.util.Pair;
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

    public static void main(String[] args) throws IOException {
        var stateRulesDb = new StateRulesDb();
        var file = new File("./examples/spacefiller.rle");
        Pair<Grid, RleFile> parsed = RleParser.parse(file, NCOLS, NROWS, StateRulesDb.ALIVE, StateRulesDb.DEAD);
        Grid theGrid = parsed.first();

        StateRulesDb.fromParsedFile(parsed.second());
        var gridEvaluator = new GridEvaluator(StateRulesDb.GAME_OF_LIFE);

        initLookAndFeel();
        AtomicBoolean running = new AtomicBoolean(false);
        SwingUtilities.invokeLater(() -> {
            var gridPane = new GridPane(theGrid, gridEvaluator, StateRulesDb.ALIVE);
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