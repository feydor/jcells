package org.fffere.jcell.rule;

import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** Manages the current StateRules */
public class StateRulesDb {
    public static final int[] STATES = new int[]{
            0x000000, 0x1f0c03, 0x3b1408, 0x65190c, 0x981b0c, 0xcb1509
    };
    public static final int ALIVE = STATES[0];
    public static final int DEAD = 0xFFFFFF;

    public static final StateRule GAME_OF_LIFE = new GameOfLifeRule(ALIVE);
    public static final StateRule STAR_WARS = new GenerationsLifeRule(new int[]{2}, new int[]{3, 4, 5},
            Arrays.stream(STATES).sequential().limit(4).toArray(),
            "Star Wars", "B2/S345/4");
    public static final StateRule TURMITE = new TurmiteRule(1, 1, ALIVE);

    public final List<StateRule> allRules = new ArrayList<>();

    public StateRulesDb() {
        allRules.add(GAME_OF_LIFE);
        allRules.add(STAR_WARS);
        allRules.add(TURMITE);
    }

    /**
     * Find a rule with the given name
     * @param ruleString to search by
     * @return the rule
     * @throws IllegalArgumentException when not found
     */
    public Optional<StateRule> findByRuleString(String ruleString) {
        return allRules.stream().filter(r -> r.ruleString().equalsIgnoreCase(ruleString)).findAny();
    }

    public StateRule[] getAllRules() {
        return allRules.toArray(allRules.toArray(new StateRule[0]));
    }

    /**
     * Creates and adds a StateRule from a pattern file. If it is already in the DB, then returns it and false.
     * @param rleFile the pattern file
     * @return True if the StateRule is new, false otherwese
     */
    public Pair<StateRule, Boolean> fromParsedFile(RleFile rleFile) {
        var found = allRules.stream().filter(rule -> rule.ruleString().equalsIgnoreCase(rleFile.ruleString())).findAny();
        if (found.isPresent())
            return new Pair<>(found.get(), false);

        var newRule = new GenerationsLifeRule(rleFile.birthConditions(), rleFile.surviveConditions(),
                STATES, rleFile.fileName(), rleFile.ruleString());
        allRules.add(newRule);
        return new Pair<>(newRule, true);
    }
}
