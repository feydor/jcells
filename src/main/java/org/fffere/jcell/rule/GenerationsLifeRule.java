package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

import java.util.Arrays;

public class GenerationsLifeRule implements GridStateRule {
    int[] birthConditions;
    int[] surviveConditions;
    int[] states;

    public GenerationsLifeRule(int[] birthConditions, int[] surviveConditions, int[] states) {
        if (birthConditions.length < 1) throw new IllegalArgumentException("Birth condition must be greater than 1.");
        if (surviveConditions.length < 1) throw new IllegalArgumentException("Survive condition is invalid.");
        if (states.length < 1) throw new IllegalArgumentException("Generations must have at least 1 value.");
        this.birthConditions = birthConditions;
        this.surviveConditions = surviveConditions;
        this.states = states;
        Arrays.sort(this.states);
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
            int state = Arrays.binarySearch(states, cell.value);
            if (state < 0)
                throw new IllegalArgumentException("Cell was found with invalid value=" + cell.value);

            // last state wraps around to dead
            if (state == states.length-1) {
                return DEAD;
            }

            return states[state+1];
        }
    }
}
