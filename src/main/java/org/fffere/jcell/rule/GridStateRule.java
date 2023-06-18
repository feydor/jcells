package org.fffere.jcell.rule;

import org.fffere.jcell.model.Cell;
import org.fffere.jcell.model.Neighbors;

public interface GridStateRule {
    int apply(Cell cell, Neighbors neighbors);
}
