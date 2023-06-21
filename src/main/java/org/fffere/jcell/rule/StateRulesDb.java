package org.fffere.jcell.rule;

import org.fffere.jcell.parser.RleFile;

import java.util.ArrayList;
import java.util.List;

public class StateRulesDb {
    public static final int[] STATES = new int[]{
            0x337AFF, 0x2252BF, 0x122A7E, 0x01023E
    };
    public static final int ALIVE = STATES[0];
    public static final int DEAD = 0xFFFFFF;

    public static final StateRule GAME_OF_LIFE = new GameOfLifeRule(ALIVE);

    /** B2/S345/4 */
    public static final StateRule STAR_WARS = new GenerationsLifeRule(new int[]{2}, new int[]{3, 4, 5}, STATES, "B2/S345/4 (Star Wars)");

    public static final List<StateRule> ALL_RULES = new ArrayList<>();

    public StateRulesDb() {
        ALL_RULES.add(GAME_OF_LIFE);
        ALL_RULES.add(STAR_WARS);
    }

    /**
     * Find a rule with the given name
     * @param name to search by
     * @return the rule
     * @throws IllegalArgumentException when not found
     */
    public static StateRule find(String name) {
        return ALL_RULES.stream().filter(r -> r.name().equalsIgnoreCase(name)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Rule with name '" + name + "' NOT found!"));
    }

    public static StateRule fromParsedFile(RleFile rleFile) {
        var rule = new GenerationsLifeRule(rleFile.birthConditions(), rleFile.surviveConditions(),
                STATES, rleFile.ruleString());
        if (ALL_RULES.stream().filter(r -> r.equals(rule)).findAny().isEmpty())
            ALL_RULES.add(rule);
        return rule;
    }
}
