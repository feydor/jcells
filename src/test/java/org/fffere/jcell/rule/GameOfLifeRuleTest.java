package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeRuleTest {
    static final int ALIVE = 0x0000FF;
    GameOfLifeRule gameOfLifeRule = new GameOfLifeRule(ALIVE);

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

    private Neighbors getTestNeighbors(int nAlive) {
        var cells = new Cell[]{
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0),
                new Cell(0, 0)
        };

        for (int i=0; i<nAlive; ++i) {
            cells[i].value = ALIVE;
        }

        return new Neighbors(cells);
    }
}