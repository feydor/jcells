package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Turmite:
 * <ol>
 *     <li>turn on the spot (by some multiple of 90°)</li>
 *     <li>change the color of the square</li>
 *     <li>move forward one square.</li>
 * </ol>
 */
public class TurmiteRule implements StateRule {
    ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    public int apply(Cell cell, Neighbors neighbors) {
        if (cell.value == DEAD) return DEAD;
        int direction = random.nextInt(0, 4);
        int randomColor = random.nextInt(0, 0xFFFF0F);
        throw new IllegalArgumentException("Unimplemented!");
    }

    @Override
    public String name() {
        return "Turmite";
    }
}
