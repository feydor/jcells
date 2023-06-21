package org.fffere.jcell.parser;

import java.util.List;

public record RleFile(String ruleString, int x, int y, int[] birthConditions, int[] surviveConditions, List<RleData> data) {
}
