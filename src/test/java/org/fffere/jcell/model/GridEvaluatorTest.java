package org.fffere.jcell.model;

import org.fffere.jcell.rule.GameOfLifeRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridEvaluatorTest {
    final static int NCOLS = 4;
    final static int NROWS = 4;
    Grid g = new Grid(NCOLS, NROWS);
    GridEvaluator gridEvaluator = new GridEvaluator(null, null);

    @Test
    void testWrapTopLeftCorner() {
        var neighbors = gridEvaluator.getNeighbors(g, 0, 0);

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
        var neighbors = gridEvaluator.getNeighbors(g, 0, NCOLS-1);

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
        var neighbors = gridEvaluator.getNeighbors(g, r, c);

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
        var neighbors = gridEvaluator.getNeighbors(g, r, c);

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
        var neighbors = gridEvaluator.getNeighbors(g, r, c);

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
        Cell center = new Cell(NROWS/2, NCOLS/2);
        GridEvaluator golEval = new GridEvaluator(new GameOfLifeRule(), null);

        // vertical blinker
        grid.set(center.row-1, center.col, GameOfLifeRule.ALIVE);
        grid.set(center.row, center.col, GameOfLifeRule.ALIVE);
        grid.set(center.row+1, center.col, GameOfLifeRule.ALIVE);

        golEval.eval(grid);

        // horizontal 3len blinker
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row-1, center.col-1));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row-1, center.col));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row-1, center.col+1));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row, center.col-1));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row, center.col));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row, center.col+1));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row+1, center.col-1));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row+1, center.col));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row+1, center.col+1));

        golEval.eval(grid);

        // vertical 3len blinker
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row-1, center.col-1));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row-1, center.col));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row-1, center.col+1));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row, center.col-1));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row, center.col));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row, center.col+1));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row+1, center.col-1));
        assertEquals(GameOfLifeRule.ALIVE, grid.get(center.row+1, center.col));
        assertEquals(GameOfLifeRule.DEAD, grid.get(center.row+1, center.col+1));
    }
}