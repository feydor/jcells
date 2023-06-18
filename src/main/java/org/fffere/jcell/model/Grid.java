package org.fffere.jcell.model;

public class Grid {
    int[][] grid;
    public static final int DEFAULT = 0xFFFFFF;
    public final int width;
    public final int height;

    public Grid(int width, int height) {
        grid = new int[width][height];
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                grid[j][i] = DEFAULT;
            }
        }
        this.width = width;
        this.height = height;
    }

    public void set(int row, int col, int value) {
        if (outOfBounds(row, col)) throw new IllegalArgumentException("Out of bounds!");
        grid[col][row] = value;
    }

    public void set(Cell cell) {
        if (outOfBounds(cell.col, cell.row)) throw new IllegalArgumentException("Out of bounds!");
        grid[cell.col][cell.row] = cell.value;
    }

    public int get(int row, int col) {
        return grid[col][row];
    }

    private boolean outOfBounds(int row, int col) {
        return col < 0 || row < 0 || col >= width || row >= height;
    }

    public Grid deepClone() {
        Grid newGrid = new Grid(width, height);
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                newGrid.grid[j][i] = grid[j][i];
            }
        }
        return newGrid;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                sb.append(Integer.toString(grid[j][i], 16)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
