package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Grid;
import org.fffere.jcell.util.Coordinate;

import java.util.concurrent.ThreadLocalRandom;

import static org.fffere.jcell.rule.RuleString.NeighborhoodType.MOORE;

/**
 * Turmite:
 * <ol>
 *     <li>turn on the spot (by some multiple of 90°)</li>
 *     <li>change the color of the square</li>
 *     <li>move forward one square.</li>
 * </ol>
 */
public class TurmiteRule implements StateRule {
    private final int alive;
    private final Turmite turmite;
    private final RuleString ruleString;

    public TurmiteRule(int row, int col, int alive) {
        this.alive = alive;
        int dir = ThreadLocalRandom.current().nextInt(0, 4);
        turmite = new Turmite(0xFF0000, dir * 90, row, col);
        ruleString = new RuleString(new int[]{0}, new int[]{0}, 2, "Termite", MOORE);
    }

    @Override
    public Grid eval(Grid currentState) {
        if (turmite.bounds() == null)
            turmite.setBounds(new Coordinate(currentState.height, currentState.width));

        int cell = currentState.get(turmite.row(), turmite.col());
        if (cell == StateRule.DEAD) {
            currentState.set(turmite.row(), turmite.col(), alive);
            turmite.turnClockwise(90);
            turmite.moveForward();
        } else {
            currentState.set(turmite.row(), turmite.col(), StateRule.DEAD);
            turmite.turnCounterClockwise(90);
            turmite.moveForward();
        }
        currentState.overlay = new Cell(turmite.row(), turmite.col(), turmite.color());
        return currentState;
    }

    @Override
    public String getName() {
        return ruleString.getName();
    }

    @Override
    public int getNumStates() {
        return ruleString.getNumStates();
    }

    @Override
    public RuleString getRuleString() {
        return ruleString;
    }
}
