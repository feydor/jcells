package org.fffere.jcell.view;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;

import javax.swing.*;
import java.awt.*;

public class GridPane extends JPanel {
    private final Grid grid;
    private final int CELL_SIZE = 30;

    public GridPane(Grid grid, GridEvaluator gridEvaluator) {
        this.grid = grid;
        TickButton tickButton = new TickButton(() -> {
            gridEvaluator.eval(grid);
            repaint();
            return null;
        });
        add(tickButton);
        setPreferredSize(new Dimension(grid.width * CELL_SIZE, grid.height * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i=0; i<grid.height; ++i) {
            for (int j=0; j<grid.width; ++j) {
                int rgb = grid.get(j, i);
                g.setColor(new Color(rgb));

                int x = i * CELL_SIZE;
                int y = j * CELL_SIZE;
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
