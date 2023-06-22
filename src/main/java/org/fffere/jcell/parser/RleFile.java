package org.fffere.jcell.parser;

import java.util.List;

record RleData(int runCount, RleTag tag) {
}

/** Representation of a Life-like pattern file */
public record RleFile(String fileName, String ruleString, int x, int y, int[] birthConditions,
                      int[] surviveConditions, List<RleData> data) {
    /** Pretty print the name and rule string */
    public String displayName() {
        return displayName(fileName, ruleString);
    }

    /** Pretty print the name and rule string */
    public static String displayName(String fileName, String ruleString) {
        return String.format("%s (%s)", ruleString, fileName);
    }

    /** Return a rule string from a display name */
    public static String extractRuleStringFromDisplayName(String displayName) {
        var tokens = displayName.split(" ");
        if (tokens.length == 1 || tokens[0].charAt(0) != 'B')
            throw new IllegalArgumentException("Display name does NOT contain a valid rule string! '" + displayName + "'");
        return tokens[0];
    }
}
