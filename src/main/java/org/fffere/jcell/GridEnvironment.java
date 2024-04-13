package org.fffere.jcell;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.rule.RuleString;
import org.fffere.jcell.rule.StateRule;
import org.fffere.jcell.rule.StateRulesDb;
import org.fffere.jcell.view.Rule2ColorMapper;

import java.io.File;
import java.io.IOException;

/** Manages all Grid-related state */
public class GridEnvironment {
    private static final int DEFAULT_DEAD_COLOR = 0xffffff;
    private static final int DEFAULT_ALIVE_COLOR = 0x0012dd   ;
    private Grid grid;
    private Grid savedGrid;
    private StateRule currentStateRule;
    private final StateRulesDb stateRulesDb;
    private boolean running = false;
    private int[] colors;


    public GridEnvironment(File rlePatternFile, int nrows, int ncols) throws IOException {
        stateRulesDb = new StateRulesDb();
        currentStateRule = StateRulesDb.GAME_OF_LIFE;
        loadFromRleFile(rlePatternFile, nrows, ncols);
    }

    public RleFile loadFromRleFile(File rlePatternFile, int nrows, int ncols) throws IOException {
        RleFile rleFile = RleFile.fromFile(rlePatternFile);
        grid = new Grid(ncols, nrows, rleFile);
        savedGrid = grid.clone();

        // the RLE file might contain a new RuleString
        createStateRule(rleFile.ruleString());
        changeRule(rleFile.ruleString());

        return rleFile;
    }

    public RleFile loadFromRleFile(File rlePatternFile) throws IOException {
        return loadFromRleFile(rlePatternFile, grid.height, grid.width);
    }

    public StateRule[] getAllRules() {
        return stateRulesDb.getAllRules();
    }

    /**
     * Returns true if added
     */
    public boolean createStateRule(RuleString ruleString) {
        return stateRulesDb.createStateRule(ruleString);
    }

    /**
     * Attempt to change the current rule
     * @param ruleString The rule string to change to
     * @throws IllegalArgumentException When a rule with the given name is NOT found
     */
    public void changeRule(RuleString ruleString) {
        StateRule rule = stateRulesDb.findByRuleString(ruleString)
                .orElseThrow(() -> new IllegalArgumentException("Rule with ruleString '" + ruleString  + "' was NOT found!"));

        System.out.println("Changing to rule: " + rule.getName());
        currentStateRule = rule;
        grid.truncateValues(currentStateRule.getNumStates() - 1);
        colors = Rule2ColorMapper.mapColorsFromRule(currentStateRule, DEFAULT_DEAD_COLOR, DEFAULT_ALIVE_COLOR);
    }

    public Grid grid() { return grid; }

    /**
     * Returns an array of rgb values that correspond to the location on the grid
     */
    public int rgbAt(int row, int column) {
        return colors[grid.get(row, column)];
    }

    public void resetState() {
        System.out.println("Resetting state...");
        grid = savedGrid.clone();
    }

    /** Run a single eval */
    public void run() {
        grid = currentStateRule.eval(grid);
    }

    public void toggleRunning() {
        System.out.println(running ? "Pausing..." : "Playing...");
        running = !running;
    }

    public boolean isRunning() {
        return running;
    }
}
