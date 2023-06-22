package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

import java.util.Arrays;

public class GenerationsLifeRule implements StateRule {
    private final int[] birthConditions;
    private final int[] surviveConditions;
    private final int[] states;
    private final String name;
    private final String ruleString;

    public GenerationsLifeRule(int[] birthConditions, int[] surviveConditions, int[] states, String name, String ruleString) {
        if (birthConditions.length < 1) throw new IllegalArgumentException("Birth condition must be greater than 1.");
        if (surviveConditions.length < 1) throw new IllegalArgumentException("Survive condition is invalid.");
        if (states.length < 1) throw new IllegalArgumentException("Generations must have at least 1 value.");
        this.birthConditions = birthConditions;
        this.surviveConditions = surviveConditions;
        this.states = states;
        this.name = name;
        this.ruleString = ruleString;
    }

    @Override
    public int apply(Cell cell, Neighbors neighbors) {
        int neighborsInStateOne = neighbors.countIf(c -> states[0] == c.value());
        if (cell.value() == DEAD) {
            var found = Arrays.stream(birthConditions).filter(b -> b == neighborsInStateOne).findAny();
            return found.isPresent() ? states[0] : DEAD;
        }

        if (cell.value() == states[0]) {
            var found = Arrays.stream(surviveConditions).filter(s -> s == neighborsInStateOne).findAny();
            return found.isPresent() ? states[0] : states[1];
        } else {
            // get the cell's state
            int state = -1;
            for (int i=0; i<states.length; ++i)
                if (states[i] == cell.value())
                    state = i;
            if (state < 0)
                throw new IllegalArgumentException("Cell was found with invalid value=" + Integer.toHexString(cell.value()));

            // last state wraps around to dead
            if (state == states.length-1) {
                return DEAD;
            }

            return states[state+1];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationsLifeRule that = (GenerationsLifeRule) o;
        return Arrays.equals(birthConditions, that.birthConditions) && Arrays.equals(surviveConditions, that.surviveConditions) && Arrays.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(birthConditions);
        result = 31 * result + Arrays.hashCode(surviveConditions);
        result = 31 * result + Arrays.hashCode(states);
        return result;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String ruleString() {
        return ruleString;
    }
}
