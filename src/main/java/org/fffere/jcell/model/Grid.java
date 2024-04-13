package org.fffere.jcell.model;

import org.fffere.jcell.parser.RleData;
import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.parser.RleTag;


public class Grid implements Cloneable {
    protected int[][] grid;
    public static final int DEFAULT = 0;
    public final int width;
    public final int height;
    public Cell overlay;

    public Grid(int width, int height, RleFile rle) {
        // Begin setting up grid, start in the relative middle
//        int startr = width/2 - rle.y()/2, startc = height/2 - rle.x()/2;
        int startr = 1;
        int startc = 1;
        if (startr + rle.y() >= height)
            throw new IllegalArgumentException("Grid width must be large enough to hold figure of y=" + rle.y());
        else if (startc + rle.x() >= width)
            throw new IllegalArgumentException("Grid height must be large enough to hold figure of x=" + rle.x());

        this.width = width;
        this.height = height;
        grid = new int[width][height];
        int r = startr, c = startc;
        for (RleData data : rle.data()) {
            if (data.tag() == RleTag.EOL) {
                // set the rest of columns in the row to dead
                // and advance to next line
                while (c < width) {
                    set(r, c++, 0);
                }
                ++r;
                c = startc;
                continue;
            }

            // fill in columns based on rle data
            for (int i=0; i<data.runCount(); ++i) {
                set(r, c++, data.tag() == RleTag.ALIVE ? 1 : 0);
            }
        }
    }

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

    public void truncateValues(int maxValue) {
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                if (grid[j][i] > maxValue)
                    grid[j][i] = maxValue;
            }
        }
    }
}
