package org.fffere.jcell.parser;

import org.fffere.jcell.rule.RuleString;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** Representation of a Life-like pattern file */
public record RleFile(String fileName, RuleString ruleString, int x, int y, List<RleData> data) {
    public static RleFile fromFile(File file) throws IOException {
        var fileNameTokens = file.getName().split("\\.");
        var ext = fileNameTokens[1];
        if (!ext.equalsIgnoreCase("rle"))
            throw new IllegalArgumentException(String.format("File '%s' not in RLE format!", file.getName()));

        // initial parse of metadata and data
        // defaults to B2/S23/2
        System.out.println("Parsing " + file.getAbsolutePath() + "...");

        String line, ruleStr = "B2/S23";
        int x = 0, y = 0;
        var dataString = new StringBuilder();
        try (var reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                line = reader.readLine();
                if (line == null) break;

                if (line.charAt(0) == '#') {
                    // a comment, do nothing
                } else if (line.charAt(0) == 'x') {
                    // first line of metadata
                    var tokens = line.replaceAll("\\s+", "").split(",");
                    if (tokens[0].charAt(0) != 'x') throw new IllegalArgumentException("Missing x parameter.");
                    x = Integer.parseInt(tokens[0].split("=")[1]);
                    if (tokens[1].charAt(0) != 'y') throw new IllegalArgumentException("Missing y parameter.");
                    y = Integer.parseInt(tokens[1].split("=")[1]);
                    if (tokens.length == 2 || tokens[2].charAt(0) != 'r') continue;
                    ruleStr = tokens[2].split("=")[1];
                } else {
                    // a line of data
                    dataString.append(line);
                }
            }
        }

        // split the data by line delimiter $
        var data = dataString.toString();
        System.out.println("x = " + x + ", y = " + y);

        var lines = data.split("((?<=\\$)|(?=\\$))"); // keep delimiter

        // Parse data string
        var rleData = new ArrayList<RleData>();
        for (String l : lines) {
            int i = 0;
            while (i != l.length()) {
                i = parseRleData(l, i, rleData);
            }
        }

        System.out.println("# of data: " + rleData.stream().filter(d -> d.tag() != RleTag.EOL).toList().size());

        // TODO: How to parse non-Moore neighbor from RLE?
        return new RleFile(fileNameTokens[0],
                RuleString.fromBirthSurvivalNotation(fileNameTokens[0], ruleStr, RuleString.NeighborhoodType.MOORE),
                x, y, rleData);
    }

    /** Pretty print the name and rule string */
    public String displayName() {
        return String.format("%s (%s)", ruleString, ruleString.getName());
    }

    /** Return a rule string from a display name like this: rule name */
    public static String extractRuleStringFromDisplayName(String displayName) {
        var tokens = displayName.split(" ");
        if (tokens.length == 1 || tokens[0].charAt(0) != 'B')
            throw new IllegalArgumentException("Display name does NOT contain a valid rule string! '" + displayName + "'");
        return tokens[0];
    }

    /**
     * Parse a single RLE data from an RLE line starting from start and return the running index
     * @param line The line to parse from
     * @param start The index in the line to start parsing from
     * @param data The list to add to
     * @return The running index
     */
    private static int parseRleData(String line, int start, List<RleData> data) {
        int i = start;
        var runCount = new StringBuilder();
        while (true) {
            char cur = line.charAt(i++);
            if (cur == 'b' || cur == 'o') {
                if (runCount.isEmpty()) runCount.append("1");
                var tag = cur == 'o' ? RleTag.ALIVE : RleTag.DEAD;
                data.add(new RleData(Integer.parseInt(runCount.toString()), tag));
                break;
            } else if (cur == '$') {
                data.add(new RleData(1, RleTag.EOL));
                break;
            } else if (cur == '!') {
                break;
            } else {
                runCount.append(cur);
            }
        }
        return i;
    }
}
