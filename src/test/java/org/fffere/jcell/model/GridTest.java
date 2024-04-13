package org.fffere.jcell.model;

import org.fffere.jcell.rule.GenerationsLifeRule;
import org.fffere.jcell.rule.StateRule;
import org.fffere.jcell.rule.StateRulesDb;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    final static int NCOLS = 4;
    final static int NROWS = 4;
    final Grid grid = new Grid(NCOLS, NROWS);

    @Test
    void testWrapTopLeftCorner() {
        var neighbors = grid.getNeighbors(0, 0);

        assertEquals(new Cell(NROWS-1, NCOLS-1), neighbors.topleft());
        assertEquals(new Cell(NROWS-1, 0), neighbors.top());
        assertEquals(new Cell(NROWS-1, 1), neighbors.topright());
        assertEquals(new Cell(0, NCOLS-1), neighbors.left());
        assertEquals(new Cell(0, 1), neighbors.right());
        assertEquals(new Cell(1, NCOLS-1), neighbors.bottomleft());
        assertEquals(new Cell(1, 0), neighbors.bottom());
        assertEquals(new Cell(1, 1), neighbors.bottomright());
    }

    @Test
    void testWrapTopRightCorner() {
        var neighbors = grid.getNeighbors(0, NCOLS-1);

        assertEquals(new Cell(NROWS-1, NCOLS-2), neighbors.topleft());
        assertEquals(new Cell(NROWS-1, NCOLS-1), neighbors.top());
        assertEquals(new Cell(NROWS-1, 0), neighbors.topright());
        assertEquals(new Cell(0, NCOLS-2), neighbors.left());
        assertEquals(new Cell(0, 0), neighbors.right());
        assertEquals(new Cell(1, NCOLS-2), neighbors.bottomleft());
        assertEquals(new Cell(1, NCOLS-1), neighbors.bottom());
        assertEquals(new Cell(1, 0), neighbors.bottomright());
    }

    @Test
    void testWrapBotLeftCorner() {
        int r = NROWS-1;
        int c = 0;
        var neighbors = grid.getNeighbors(r, c);

        assertEquals(new Cell(r-1, NCOLS-1), neighbors.topleft());
        assertEquals(new Cell(r-1, c), neighbors.top());
        assertEquals(new Cell(r-1, c+1), neighbors.topright());
        assertEquals(new Cell(r, NCOLS-1), neighbors.left());
        assertEquals(new Cell(r, c+1), neighbors.right());
        assertEquals(new Cell(0, NCOLS-1), neighbors.bottomleft());
        assertEquals(new Cell(0, 0), neighbors.bottom());
        assertEquals(new Cell(0, c+1), neighbors.bottomright());
    }

    @Test
    void testWrapBotRightCorner() {
        int r = NROWS-1;
        int c = NCOLS-1;
        var neighbors = grid.getNeighbors(r, c);

        assertEquals(new Cell(r-1, c-1), neighbors.topleft());
        assertEquals(new Cell(r-1, c), neighbors.top());
        assertEquals(new Cell(r-1, 0), neighbors.topright());
        assertEquals(new Cell(r, c-1), neighbors.left());
        assertEquals(new Cell(r, 0), neighbors.right());
        assertEquals(new Cell(0, c-1), neighbors.bottomleft());
        assertEquals(new Cell(0, c), neighbors.bottom());
        assertEquals(new Cell(0, 0), neighbors.bottomright());
    }

    @Test
    void testInteriorPoint() {
        int r = 1;
        int c = 1;
        var neighbors = grid.getNeighbors(r, c);

        assertEquals(new Cell(r-1, c-1), neighbors.topleft());
        assertEquals(new Cell(r-1, c), neighbors.top());
        assertEquals(new Cell(r-1, c+1), neighbors.topright());
        assertEquals(new Cell(r, c-1), neighbors.left());
        assertEquals(new Cell(r, c+1), neighbors.right());
        assertEquals(new Cell(r+1, c-1), neighbors.bottomleft());
        assertEquals(new Cell(r+1, c), neighbors.bottom());
        assertEquals(new Cell(r+1, c+1), neighbors.bottomright());
    }

    @Test
    void testBlinker() {
        Grid grid = new Grid(NCOLS, NROWS);
        int r = NROWS/2, c = NCOLS/2, alive = 0x0000FF;
        StateRule gameOfLife = StateRulesDb.GAME_OF_LIFE;

        // vertical blinker
        grid.set(r-1, c, alive);
        grid.set(r, c, alive);
        grid.set(r+1, c, alive);

        grid = gameOfLife.eval(grid);

        // horizontal 3len blinker
        assertEquals(StateRule.DEAD, grid.get(r-1, c-1));
        assertEquals(StateRule.DEAD, grid.get(r-1, c));
        assertEquals(StateRule.DEAD, grid.get(r-1, c+1));
        assertEquals(alive, grid.get(r, c-1));
        assertEquals(alive, grid.get(r, c));
        assertEquals(alive, grid.get(r, c+1));
        assertEquals(StateRule.DEAD, grid.get(r+1, c-1));
        assertEquals(StateRule.DEAD, grid.get(r+1, c));
        assertEquals(StateRule.DEAD, grid.get(r+1, c+1));

        grid = gameOfLife.eval(grid);

        // vertical 3len blinker
        assertEquals(StateRule.DEAD, grid.get(r-1, c-1));
        assertEquals(alive, grid.get(r-1, c));
        assertEquals(StateRule.DEAD, grid.get(r-1, c+1));
        assertEquals(StateRule.DEAD, grid.get(r, c-1));
        assertEquals(alive, grid.get(r, c));
        assertEquals(StateRule.DEAD, grid.get(r, c+1));
        assertEquals(StateRule.DEAD, grid.get(r+1, c-1));
        assertEquals(alive, grid.get(r+1, c));
        assertEquals(StateRule.DEAD, grid.get(r+1, c+1));
    }
}