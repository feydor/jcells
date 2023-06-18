package org.fffere.jcell.model;

import java.util.Objects;

public class Cell {
    public int row;
    public int col;
    public int value;

    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = Grid.DEFAULT;
    }

    public boolean sameCoordinates(Cell other) {
        return other.row == row && other.col == col;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col && value == cell.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value);
    }
}
