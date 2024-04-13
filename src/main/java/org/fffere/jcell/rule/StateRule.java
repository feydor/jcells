package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.Neighbors;

import java.util.function.BiFunction;

public interface StateRule {
    int DEAD = 0;

    /** Generates the next state */
    Grid eval(Grid currentState);
    String getName();
    /**
     * Evaluate the next state by iterating across the cells and its neighbors
     * @param grid the grid state
     * @param rule the rule to apply
     * @return the next state
     */
    static Grid cellByCellEval(Grid grid, BiFunction<Cell, Neighbors, Integer> rule) {
        var nextState = new Grid(grid.width, grid.height);
        for (int row=0; row<grid.height; ++row) {
            for (int col=0; col<grid.width; ++col) {
                var cell = new Cell(row, col, grid.get(row, col));
                var neighbors = grid.getNeighbors(row, col);
                int nextValue = rule.apply(cell, neighbors);
                nextState.set(row, col, nextValue);
            }
        }
        return nextState;
    }

    int getNumStates();

    RuleString getRuleString();
}
