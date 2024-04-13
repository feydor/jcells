package org.fffere.jcell.rule;

import java.util.Arrays;
import java.util.Objects;

public class RuleString {
    /**
     * The list of numbers of live neighbors that cause a dead cell to become alive.
     */
    private final int[] birthConditions;

    /**
     * The list of numbers of live neighbors that keep a live cell alive.
     */
    private final int[] surviveConditions;

    private final int numStates;

    private final String name;

    private final NeighborhoodType neighborhoodType;

    public enum NeighborhoodType {
        VON_NEUMANN,
        HEXAGONAL,
        MOORE
    }

    public RuleString(int[] birthConditions, int[] surviveConditions, int numStates, String name, NeighborhoodType neighborhoodType) {
        if (birthConditions.length < 1) throw new IllegalArgumentException("Birth condition must have at least 1 value.");
        if (surviveConditions.length < 1) throw new IllegalArgumentException("Survive condition must have at least 1 value.");
        if (numStates < 2) throw new IllegalArgumentException("numStates must be at least 2.");

        this.birthConditions = birthConditions;
        this.surviveConditions = surviveConditions;
        this.numStates = numStates;
        this.name = name;
        this.neighborhoodType = neighborhoodType;
    }

    // Utility methods to parse different rulestring formats (B/S, S/B, etc.)
    public static RuleString fromBirthSurvivalNotation(String name, String notation, NeighborhoodType neighborhoodType) throws IllegalArgumentException {
        // Implement parsing logic for B/S notation
        String[] tokens = notation.split("/");
        int[] birthConditions = extractDigits(tokens[0]);
        int[] surviveConditions = extractDigits(tokens[1]);
        int numStates = tokens.length > 2 ? Integer.parseInt(tokens[2]) : 2;
        return new RuleString(birthConditions, surviveConditions, numStates, name, neighborhoodType);
    }

    private static final String BSC_TOKENS = "BSCG";
    private static int[] extractDigits(String ruleStr) {
        if (BSC_TOKENS.chars().noneMatch(ch -> ruleStr.charAt(0) == (char)ch))
            throw new IllegalArgumentException(String.format("BSC rule string %s did not begin with one of %s", ruleStr, BSC_TOKENS));

        int[] digits = new int[ruleStr.length()-1];
        for (int i=1; i<ruleStr.length(); ++i)
            digits[i-1] = ruleStr.charAt(i) - '0';
        return digits;
    }

    public String getName() {
        return name;
    }

    public String getRuleString() {
        return "B" + String.join("", Arrays.stream(birthConditions).mapToObj(String::valueOf).toList())
                + "/S" + String.join("", Arrays.stream(surviveConditions).mapToObj(String::valueOf).toList())
                + (numStates == 2 ? "" : "/" + numStates);
    }

    @Override
    public String toString() {
        return getRuleString() + " (" + name + ")";
    }

    public int[] getBirthConditions() {
        return birthConditions;
    }

    public int[] getSurviveConditions() {
        return surviveConditions;
    }

    public int getNumStates() {
        return numStates;
    }

    public NeighborhoodType getNeighborhoodType() {
        return neighborhoodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleString that = (RuleString) o;
        return that.getRuleString().equalsIgnoreCase(this.getRuleString());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numStates);
        result = 31 * result + Arrays.hashCode(birthConditions);
        result = 31 * result + Arrays.hashCode(surviveConditions);
        return result;
    }
}
