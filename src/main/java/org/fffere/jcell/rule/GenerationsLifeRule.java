package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

import java.util.Arrays;

public class GenerationsLifeRule implements StateRule {
    private final int[] birthConditions;
    private final int[] surviveConditions;
    private final int[] states;

    public GenerationsLifeRule(int[] birthConditions, int[] surviveConditions, int[] states) {
        if (birthConditions.length < 1) throw new IllegalArgumentException("Birth condition must be greater than 1.");
        if (surviveConditions.length < 1) throw new IllegalArgumentException("Survive condition is invalid.");
        if (states.length < 1) throw new IllegalArgumentException("Generations must have at least 1 value.");
        this.birthConditions = birthConditions;
        this.surviveConditions = surviveConditions;
        this.states = states;
    }

    @Override
    public int apply(Cell cell, Neighbors neighbors) {
        int neighborsInStateOne = neighbors.countIf(c -> states[0] == c.value);
        if (cell.value == DEAD) {
            var found = Arrays.stream(birthConditions).filter(b -> b == neighborsInStateOne).findAny();
            return found.isPresent() ? states[0] : DEAD;
        }

        if (cell.value == states[0]) {
            var found = Arrays.stream(surviveConditions).filter(s -> s == neighborsInStateOne).findAny();
            return found.isPresent() ? states[0] : states[1];
        } else {
            // get the cell's state
            int state = -1;
            for (int i=0; i<states.length; ++i)
                if (states[i] == cell.value)
                    state = i;
            if (state < 0)
                throw new IllegalArgumentException("Cell was found with invalid value=" + Integer.toHexString(cell.value));

            // last state wraps around to dead
            if (state == states.length-1) {
                return DEAD;
            }

            return states[state+1];
        }
    }

    @Override
    public String name() {
        return "Generations";
    }
}
