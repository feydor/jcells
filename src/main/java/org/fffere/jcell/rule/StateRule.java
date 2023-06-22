package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

public interface StateRule {
    int DEAD = StateRulesDb.DEAD;

    int apply(Cell cell, Neighbors neighbors);
    String name();
    String ruleString();
}
