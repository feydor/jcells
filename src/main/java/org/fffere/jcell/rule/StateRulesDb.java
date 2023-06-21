package org.fffere.jcell.rule;

import java.util.List;

public class StateRulesDb {
    public static final int[] STATES = new int[]{
            0x0059FF, 0x003BAA, 0x001E55, 0x000000
    };
    public static final int ALIVE = STATES[0];
    public static final int DEAD = 0xFFFFFF;

    public static final StateRule GAME_OF_LIFE = new GameOfLifeRule(ALIVE);

    /** B2/S345/4 */
    public static final StateRule STAR_WARS = new GenerationsLifeRule(new int[]{2}, new int[]{3, 4, 5}, STATES);

    public static final List<StateRule> ALL_RULES = List.of(GAME_OF_LIFE, STAR_WARS);

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
}
