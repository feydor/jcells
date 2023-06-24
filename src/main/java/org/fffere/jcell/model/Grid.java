package org.fffere.jcell.model;

import org.fffere.jcell.rule.StateRulesDb;

public class Grid implements Cloneable {
    protected int[][] grid;
    public static final int DEFAULT = StateRulesDb.DEAD;
    public final int width;
    public final int height;
    public Cell overlay;

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
        grid[col][row] = value;
    }

    public int get(int row, int col) {
        return grid[col][row];
    }

    /** Get the Moore neighborhood of a cell. The surrounding cells with wrap-around. */
    public Neighbors getNeighbors(int row, int col) {
        // surrounding cells, clockwise starting from upper-left
        var neighbors = new Cell[8];
        int w = width;
        int h = height;
        int r, c;

        // upper-left
        r = row-1 < 0 ? h-1 : row-1; // shift up
        c = col-1 < 0 ? w-1 : col-1; // shift left
        neighbors[0] = new Cell(r, c, get(r, c));

        // up
        r = row-1 < 0 ? h-1 : row-1; // shift up
        neighbors[1] = new Cell(r, col, get(r, col));

        // upper-right
        r = row-1 < 0 ? h-1 : row-1; // shift up
        c = col+1 >= w ? 0 : col+1; // shift right
        neighbors[2] = new Cell(r, c, get(r, c));

        // left
        c = col-1 < 0 ? w-1 : col-1;
        neighbors[3] = new Cell(row, c, get(row, c));

        // right
        c = col+1 >= w ? 0 : col+1;
        neighbors[4] = new Cell(row, c, get(row, c));

        // bottom-left
        r = row+1 >= h ? 0 : row+1; // shift down
        c = col-1 < 0 ? w-1 : col-1; // shift left
        neighbors[5] = new Cell(r, c, get(r, c));

        // bottom
        r = row+1 >= h ? 0 : row+1;
        neighbors[6] = new Cell(r, col, get(r, col));

        // bottom-right
        r = row+1 >= h ? 0 : row+1; // shift down
        c = col+1 >= w ? 0 : col+1; // shift right
        neighbors[7] = new Cell(r, c, get(r, c));
        return new Neighbors(neighbors);
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                var box = grid[j][i] == DEFAULT ? "□" : "■";
                sb.append(box);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Grid clone() {
        try {
            Grid clone = (Grid) super.clone();
            // Deep copy mutable state
            clone.grid = new int[width][height];
            for (int i=0; i<height; ++i) {
                for (int j=0; j<width; ++j) {
                    clone.grid[j][i] = grid[j][i];
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /** returns the overlay if the given coordinate has an active overlay for it, otherwise null */
    public Integer getOverlay(int j, int i) {
        return overlay != null && overlay.hasCoordinate(j, i) ? overlay.value() : null;
    }
}
