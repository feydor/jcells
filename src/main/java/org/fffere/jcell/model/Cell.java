package org.fffere.jcell.model;

public record Cell(int row, int col, int value) {
    public Cell(int row, int col) {
        this(row , col, Grid.DEFAULT);
    }
}
