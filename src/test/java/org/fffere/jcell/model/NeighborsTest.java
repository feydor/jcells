package org.fffere.jcell.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeighborsTest {

    @Test
    void testCountIf() {
        int value = 1;
        var neighbors = new Neighbors(new Cell[]{
                new Cell(0, 0, value),
                new Cell(0, 0, value),
                new Cell(0, 0, value),
                new Cell(0, 0),
                new Cell(0, 0, value),
                new Cell(0, 0, value),
                new Cell(0, 0, value),
                new Cell(0, 0, value)
        });

        int res = neighbors.countIf(cell -> cell.value == value);
        assertEquals(7, res);
    }
}
