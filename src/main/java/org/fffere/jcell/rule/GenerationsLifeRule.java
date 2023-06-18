package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

public class GenerationsLifeRule implements GridStateRule {
    int birthCondition;
    int surviveCondition;
    int[] generations;

    public GenerationsLifeRule(int birthCondition, int surviveCondition, int[] generations) {
        if (birthCondition < 1) throw new IllegalArgumentException("Birth condition must be greater than 1.");
        if (surviveCondition < 0) throw new IllegalArgumentException("Survive condition is invalid.");
        if (generations.length < 1) throw new IllegalArgumentException("Generations must have at least 1 value.");
        this.birthCondition = birthCondition;
        this.surviveCondition = surviveCondition;
        this.generations = generations;
    }

    @Override
    public int apply(Cell cell, Neighbors neighbors) {
        int neighborsInStateOne = neighbors.countIf(c -> generations[0] == c.value);
        if (cell.value == DEAD) {
            return neighborsInStateOne == birthCondition ? generations[0] : DEAD;
        }

        if (cell.value == generations[0]) {
            if (neighborsInStateOne == surviveCondition) {
                return generations[0];
            } else {
                return generations[1];
            }
        } else {
            if (cell.value == generations[generations.length-1]) {
                return DEAD;
            }
            return generations[(cell.value+1) % generations.length-1];
        }
    }
}
