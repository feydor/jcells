package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.Neighbors;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * B/S/C format:
 * <ol>
 *     <li>A state 0 cell advances to state 1 if the # of neighbors is present in its birth conditions (B)</li>
 *     <li>
 *         A state 1 cell will either:
 *         <ul>
 *             <li>Remain in the same state if the # of neighbors is present in its survive conditions (S)</li>
 *             <li>Otherwise advance to state 2</lo>
 *         </ul>
 *     </li>
 *     <li>A state m >= 2 cell will advance to state ((m+1) mod C). A cell in state C resets to 0 ("dies")</li>
 * </ol>
 * Source: <a href="https://conwaylife.com/wiki/Generations">Generations</a>
 */
public class GenerationsLifeRule implements StateRule {
    private final int[] birthConditions;
    private final int[] surviveConditions;
    private final int states;
    private final RuleString ruleString;

    public GenerationsLifeRule(RuleString ruleString) {
        this.ruleString = ruleString;
        this.birthConditions = ruleString.getBirthConditions();
        this.surviveConditions = ruleString.getSurviveConditions();
        this.states = ruleString.getNumStates();
    }

    @Override
    public int getNumStates() {
        return states;
    }

    @Override
    public RuleString getRuleString() {
        return ruleString;
    }

    @Override
    public Grid eval(Grid currentState) {
        return StateRule.cellByCellEval(currentState, this::apply);
    }

    public int apply(Cell cell, Neighbors neighbors) {
        int neighborsInStateOne = neighbors.countIf(c -> c.value() == 1);

        int state = cell.value();
        if (state == 0) {
            var found = Arrays.stream(birthConditions).filter(b -> b == neighborsInStateOne).findAny();
            return found.isPresent() ? 1 : 0;
        } else if (state == 1) {
            var found = Arrays.stream(surviveConditions).filter(s -> s == neighborsInStateOne).findAny();
            return found.isPresent() ? 1 : 2 % states;
        } else {
            if (state < 0) {
                throw new IllegalArgumentException("Cell was found with negative state: " + state);
            } else if (state >= states) {
                // truncate states higher than allowable
                state = states - 1;
            }
            return (state + 1) % states;
        }
    }

    @Override
    public String getName() {
        return ruleString.getName();
    }

    @Override
    public String toString() {
        return ruleString.toString();
    }
}
