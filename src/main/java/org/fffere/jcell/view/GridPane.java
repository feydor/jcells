package org.fffere.jcell.view;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.StateRule;
import org.fffere.jcell.rule.StateRulesDb;
import org.fffere.jcell.util.Pair;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GridPane extends JPanel {
    public Grid grid;
    private final GridEvaluator gridEvaluator;
    private static final int MARGIN = 20;
    private static final int CELL_SIZE = 10;
    public final Dimension size;
    private final int alive;

    public GridPane(Grid grid, GridEvaluator gridEvaluator, int alive) {
        this.grid = grid;
        this.gridEvaluator = gridEvaluator;
        this.alive = alive;
        size = new Dimension((grid.width * CELL_SIZE) - 2*MARGIN,
                (grid.height * CELL_SIZE) - 2*MARGIN);
        setPreferredSize(size);
        var paddingBorder = new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
        var solidBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        var compoundBorder = new CompoundBorder(solidBorder, paddingBorder);
        setBorder(compoundBorder);
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
                g.setColor(Color.GRAY);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public RleFile loadPatternFile(File file) throws IOException {
        Pair<Grid, RleFile> parsed = RleParser.parse(file, grid.width, grid.height, alive, Grid.DEFAULT);
        grid = parsed.first();
        changeRule(StateRulesDb.fromParsedFile(parsed.second()));
        repaint();
        return parsed.second();
    }

    public void changeRule(StateRule rule) {
        System.out.println("Changing to rule: " + rule.name());
        gridEvaluator.setStateRule(rule);
        repaint();
    }
}
