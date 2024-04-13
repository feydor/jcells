package org.fffere.jcell.model;

import java.util.Arrays;
import java.util.function.Predicate;

/** All eight neighboring cells */
public class Neighbors {
    private final Cell[] neighbors;

    public Neighbors(Cell[] neighbors) {
        if (neighbors.length != 8) throw new IllegalArgumentException("# of neighbors was NOT 8");
        this.neighbors = neighbors;
    }

    public Cell topleft() {
        return neighbors[0];
    }
    public Cell top() {
        return neighbors[1];
    }
    public Cell topright() {
        return neighbors[2];
    }
    public Cell left() {
        return neighbors[3];
    }
    public Cell right() {
        return neighbors[4];
    }
    public Cell bottomleft() {
        return neighbors[5];
    }
    public Cell bottom() {
        return neighbors[6];
    }
    public Cell bottomright() {
        return neighbors[7];
    }

    public int countIf(Predicate<Cell> predicate) {
        int count = 0;
        for (var neighbor : neighbors) {
            if (predicate.test(neighbor)) {
                count++;
            }
        }
        return count;
    }
}
