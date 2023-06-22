package org.fffere.jcell;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.model.GridEvaluator;
import org.fffere.jcell.parser.RleFile;
import org.fffere.jcell.parser.RleParser;
import org.fffere.jcell.rule.StateRule;
import org.fffere.jcell.rule.StateRulesDb;
import org.fffere.jcell.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/** Manages all Grid-related state */
public class GridEnvironment {
    private Grid grid;
    private Grid savedGrid;
    private final GridEvaluator gridEvaluator;
    private RleFile currentRleFile;
    private final StateRulesDb stateRulesDb;
    private boolean running = false;

    public GridEnvironment(Grid initialGrid, GridEvaluator gridEvaluator, StateRulesDb stateRulesDb, RleFile initialRleFile) {
        grid = initialGrid;
        savedGrid = initialGrid;
        this.gridEvaluator = gridEvaluator;
        this.stateRulesDb = stateRulesDb;
        currentRleFile = initialRleFile;
    }

    public GridEnvironment(File patternFile, int nrows, int ncols) {
        // TODO: Only parses RLE files for now
        Pair<Grid, RleFile> parsed;
        try {
            parsed = RleParser.parse(patternFile, ncols, nrows,
                    StateRulesDb.ALIVE, Grid.DEFAULT);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        grid = parsed.first();
        savedGrid = grid;
        currentRleFile = parsed.second();
        stateRulesDb = new StateRulesDb();
        Pair<StateRule, Boolean> res = stateRulesDb.fromParsedFile(currentRleFile);
        gridEvaluator = new GridEvaluator(res.first());
    }

    public StateRule[] getAllCurrentRules() {
        return stateRulesDb.getAllRules();
    }

    /**
     * Loads the state from a pattern file
     *
     * @return The RleFile if the pattern file was new
     */
    public Optional<RleFile> loadPatternFile(File file) {
        Pair<Grid, RleFile> parsed;

        try {
            parsed = RleParser.parse(file, grid.width, grid.height,
                    StateRulesDb.ALIVE, Grid.DEFAULT);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        grid = parsed.first();
        currentRleFile = parsed.second();
        Pair<StateRule, Boolean> res = stateRulesDb.fromParsedFile(currentRleFile);
        gridEvaluator.setStateRule(res.first());
        return Optional.ofNullable(res.second() ? currentRleFile : null);
    }

    /**
     * Attempt to change the current rule
     * @param ruleString The rule string to change to
     * @throws IllegalArgumentException When a rule with the given name is NOT found
     */
    public void changeRule(String ruleString) {
        StateRule rule = stateRulesDb.findByRuleString(ruleString)
                .orElseThrow(() -> new IllegalArgumentException("Rule with ruleString '" + ruleString  + "' was NOT found!"));
        System.out.println("Changing to rule: " + rule.name());
        gridEvaluator.setStateRule(rule);
    }

    public Grid grid() { return grid; }

    public void resetState() {
        System.out.println("Resetting state...");
        grid = savedGrid;
    }

    public void run() {
        gridEvaluator.eval(grid);
    }

    public void startRunning(int interval) {
        System.out.println("Starting run at interval: " + interval);
        var task = new TimerTask() {
            @Override
            public void run() {
                if (running) run();
            }
        };
        var timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, interval);
        running = true;
    }

    public void toggleRunning() {
        System.out.println(running ? "Pausing..." : "Playing...");
        running = !running;
    }

    public boolean isRunning() {
        return running;
    }
}
