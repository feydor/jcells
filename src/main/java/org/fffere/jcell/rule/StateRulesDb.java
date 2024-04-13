package org.fffere.jcell.rule;

import java.util.*;

/** Manages the current StateRules */
public class StateRulesDb {

    public static final StateRule GAME_OF_LIFE = new GenerationsLifeRule(
            new RuleString(new int[]{3}, new int[]{2, 3}, 2, "Game Of Life", RuleString.NeighborhoodType.MOORE)
    );
    public static final StateRule STAR_WARS = new GenerationsLifeRule(
            new RuleString(new int[]{2}, new int[]{3, 4, 5}, 4, "Star Wars", RuleString.NeighborhoodType.MOORE)
    );
    public static final StateRule TURMITE = new TurmiteRule(1, 1, 1);

    private final Map<RuleString, StateRule> stateRules = new HashMap<>();

    public StateRulesDb() {
        stateRules.put(GAME_OF_LIFE.getRuleString(), GAME_OF_LIFE);
        stateRules.put(STAR_WARS.getRuleString(), STAR_WARS);
        stateRules.put(TURMITE.getRuleString(), TURMITE);
    }

    /**
     * Find a rule with the given name
     * @param ruleString to search by
     * @return the rule
     * @throws IllegalArgumentException when not found
     */
    public Optional<StateRule> findByRuleString(RuleString ruleString) {
        return Optional.ofNullable(stateRules.get(ruleString));
    }

    public StateRule[] getAllRules() {
        return stateRules.values().toArray(new StateRule[0]);
    }

    /**
     * @return true if created
     */
    public boolean createStateRule(RuleString ruleString) {
        if (findByRuleString(ruleString).isPresent())
            return false;

        stateRules.put(ruleString, new GenerationsLifeRule(ruleString));
        return true;
    }
}
