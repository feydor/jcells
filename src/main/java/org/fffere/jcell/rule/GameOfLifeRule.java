package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

/**
 * Conway's Game of Life:
 * <ol>
 *     <li>Any live cell with fewer than two live neighbours dies, as if by underpopulation.</li>
 *     <li>Any live cell with two or three live neighbours lives on to the next generation.</li>
 *     <li>Any live cell with more than three live neighbours dies, as if by overpopulation.</li>
 *     <li>Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction</li>
 * </ol>
 */
public class GameOfLifeRule implements StateRule {

    private final int alive;

    public GameOfLifeRule(int alive) {
        this.alive = alive;
    }

    @Override
    public int apply(Cell cell, Neighbors neighbors) {
        int neighborsAlive = neighbors.countIf(c -> alive == c.value());
        if (cell.value() == alive && (neighborsAlive == 2 || neighborsAlive == 3)) {
            return alive;
        } else if (cell.value() == DEAD && neighborsAlive == 3) {
            return alive;
        } else {
            return DEAD;
        }
    }

    @Override
    public String name() {
        return "Game of Life";
    }

    @Override
    public String ruleString() {
        return "B3/S23";
    }
}
