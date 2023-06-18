package org.fffere;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.rule.GameOfLifeRule;
import org.fffere.jcell.view.GridPane;
import org.fffere.jcell.view.TickButton;

import javax.swing.*;

public class Main {
    static final int NROWS = 25;
    static final int NCOLS = 25;
    public static void main(String[] args) {
        System.out.println("Yeet World!!");
        Grid theGrid = new Grid(NROWS, NCOLS);
        GridEvaluator gridEvaluator = new GridEvaluator(new GameOfLifeRule(), null);
        theGrid.set(NROWS/2, NCOLS/2, GameOfLifeRule.ALIVE);
        theGrid.set((NROWS/2)-1, NCOLS/2, GameOfLifeRule.ALIVE);
        theGrid.set((NROWS/2)+1, NCOLS/2, GameOfLifeRule.ALIVE);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JCells");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            var gridPane = new GridPane(theGrid, gridEvaluator);
            frame.getContentPane().add(gridPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}