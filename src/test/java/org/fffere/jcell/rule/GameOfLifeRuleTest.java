package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeRuleTest {
    static final int ALIVE = 0x0000FF;
    final GameOfLifeRule gameOfLifeRule = new GameOfLifeRule(ALIVE);

    @Test
    void testSingleCell() {
        var cell = new Cell(10, 10, ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(0));
        assertEquals(StateRule.DEAD, res);
    }

    @Test
    void testThreeAliveNeighbors() {
        var cell = new Cell(10, 10, ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(3));
        assertEquals(ALIVE, res);
    }

    @Test
    void testDeadCellWithThreeAliveNeighbors() {
        var cell = new Cell(10, 10, StateRule.DEAD);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(3));
        assertEquals(ALIVE, res);
    }

    @Test
    void testMoreThanThreeNeighbors() {
        var cell = new Cell(10, 10, ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(4));
        assertEquals(StateRule.DEAD, res);
    }

    /** Get a full set of neighbors (8) with nAlive being ALIVE */
    private Neighbors getTestNeighbors(int nAlive) {
        var cells = new Cell[8];

        // Set the first nAlive to ALIVE
        int i;
        for (i=0; i<nAlive; ++i) {
            cells[i] = new Cell(0, 0, ALIVE);
        }

        // The rest are dead by default
        for (int j=i; j<cells.length; ++j) {
            cells[j] = new Cell(0, 0);
        }

        return new Neighbors(cells);
    }
}