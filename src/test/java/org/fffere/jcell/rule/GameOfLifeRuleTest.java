package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeRuleTest {
    GameOfLifeRule gameOfLifeRule = new GameOfLifeRule();

    @Test
    void testSingleCell() {
        var cell = new Cell(10, 10, GridStateRule.ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(0));
        assertEquals(GridStateRule.DEAD, res);
    }

    @Test
    void testThreeAliveNeighbors() {
        var cell = new Cell(10, 10, GridStateRule.ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(3));
        assertEquals(GridStateRule.ALIVE, res);
    }

    @Test
    void testDeadCellWithThreeAliveNeighbors() {
        var cell = new Cell(10, 10, GridStateRule.DEAD);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(3));
        assertEquals(GridStateRule.ALIVE, res);
    }

    @Test
    void testMoreThanThreeNeighbors() {
        var cell = new Cell(10, 10, GridStateRule.ALIVE);
        var res = gameOfLifeRule.apply(cell, getTestNeighbors(4));
        assertEquals(GridStateRule.DEAD, res);
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
            cells[i].value = GridStateRule.ALIVE;
        }

        return new Neighbors(cells);
    }
}