package org.fffere.jcell.model;

import org.fffere.jcell.rule.StateRulesDb;

public class Grid {
    protected int[][] grid;
    public static final int DEFAULT = StateRulesDb.DEAD;
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
        grid[col][row] = value;
    }

    public int get(int row, int col) {
        return grid[col][row];
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
}
