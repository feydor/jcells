package org.fffere.jcell.model;

public record Cell(int row, int col, int value) {
    public Cell(int row, int col) {
        this(row , col, Grid.DEFAULT);
    }

    /** returns true if both have the same coordinate */
    public boolean hasCoordinate(int row, int col) {
        return this.row == row && this.col == col;
    }
}
