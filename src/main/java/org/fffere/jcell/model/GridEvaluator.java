package org.fffere.jcell.model;

import org.fffere.jcell.rule.StateRule;

/** Applies the current rule and generates the next Grid */
public class GridEvaluator {
    private StateRule stateRule;

    public GridEvaluator(StateRule stateRule) {
        this.stateRule = stateRule;
    }

    public void eval(Grid currentState) {
        Grid nextState = new Grid(currentState.width, currentState.height);
        for (int row=0; row<currentState.height; ++row) {
            for (int col=0; col<currentState.width; ++col) {
                var cell = new Cell(row, col, currentState.get(row, col));
                var neighbors = getNeighbors(currentState, row, col);
                int nextValue = stateRule.apply(cell, neighbors);
                nextState.set(row, col, nextValue);
            }
        }

        currentState.grid = nextState.grid;
    }

    public Neighbors getNeighbors(Grid grid, int row, int col) {
        // surrounding cells, clockwise starting from upper-left
        var neighbors = new Cell[8];
        int w = grid.width;
        int h = grid.height;
        int r, c;

        // upper-left
        r = row-1 < 0 ? h-1 : row-1; // shift up
        c = col-1 < 0 ? w-1 : col-1; // shift left
        neighbors[0] = new Cell(r, c, grid.get(r, c));

        // up
        r = row-1 < 0 ? h-1 : row-1; // shift up
        neighbors[1] = new Cell(r, col, grid.get(r, col));

        // upper-right
        r = row-1 < 0 ? h-1 : row-1; // shift up
        c = col+1 >= w ? 0 : col+1; // shift right
        neighbors[2] = new Cell(r, c, grid.get(r, c));

        // left
        c = col-1 < 0 ? w-1 : col-1;
        neighbors[3] = new Cell(row, c, grid.get(row, c));

        // right
        c = col+1 >= w ? 0 : col+1;
        neighbors[4] = new Cell(row, c, grid.get(row, c));

        // bottom-left
        r = row+1 >= h ? 0 : row+1; // shift down
        c = col-1 < 0 ? w-1 : col-1; // shift left
        neighbors[5] = new Cell(r, c, grid.get(r, c));

        // bottom
        r = row+1 >= h ? 0 : row+1;
        neighbors[6] = new Cell(r, col, grid.get(r, col));

        // bottom-right
        r = row+1 >= h ? 0 : row+1; // shift down
        c = col+1 >= w ? 0 : col+1; // shift right
        neighbors[7] = new Cell(r, c, grid.get(r, c));
        return new Neighbors(neighbors);
    }

    public void setStateRule(StateRule stateRule) {
        this.stateRule = stateRule;
    }
}
