package org.fffere.jcell.view;

import org.fffere.jcell.GridEnvironment;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Objects;

/** Draws the Grid */
public class GridPane extends JPanel {
    public final GridEnvironment gEnv;
    private static final int MARGIN = 20;
    private static final int CELL_SIZE = 10;
    public final Dimension size;

    public GridPane(GridEnvironment gridEnvironment) {
        this.gEnv = gridEnvironment;
        size = new Dimension((gEnv.grid().width * CELL_SIZE) - 2*MARGIN,
                (gEnv.grid().height * CELL_SIZE) - 2*MARGIN);
        setPreferredSize(size);
        var paddingBorder = new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
        var solidBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        var compoundBorder = new CompoundBorder(solidBorder, paddingBorder);
        setBorder(compoundBorder);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i=0; i<gEnv.grid().height; ++i) {
            for (int j=0; j<gEnv.grid().width; ++j) {
                int rgb = gEnv.grid().get(j, i);

                var overlay = gEnv.grid().getOverlay(j, i);
                g.setColor(new Color(Objects.requireNonNullElse(overlay, rgb)));

                int x = i * CELL_SIZE;
                int y = j * CELL_SIZE;
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
