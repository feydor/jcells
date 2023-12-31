package org.fffere.jcell.parser;

import org.fffere.jcell.model.Grid;
import org.fffere.jcell.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Load a grid from a rle file */
public class RleParser {

    /** Parse a Grid from a patterns file */
    public static Pair<Grid, RleFile> parse(File file, int ncols, int nrows, int alive, int dead) throws IOException {
        var ext = file.getName().split("\\.")[1];
        if (!ext.equalsIgnoreCase("rle"))
            throw new IllegalArgumentException(String.format("File '%s' not in RLE format!", file.getName()));
        var reader = new BufferedReader(new FileReader(file));

        // initial parse of metadata and data
        System.out.println("Parsing " + file.getAbsolutePath() + "...");
        String line, ruleStr = "B2/S23";
        int x = 0, y = 0;
        int[] birthConditions = new int[]{2}, surviveConditions = new int[]{2, 3};
        var dataString = new StringBuilder();
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
                var ruleTokens = ruleStr.split("/");
                birthConditions = extractDigits(ruleTokens[0]);
                surviveConditions = extractDigits(ruleTokens[1]);
            } else {
                // a line of data
                dataString.append(line);
            }
        }
        reader.close();

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

        // Begin setting up grid
        // start in the relative middle
        int startr = ncols/2 - y/2, startc = nrows/2 - x/2;
        if (startr + y >= nrows)
            throw new IllegalArgumentException("nrows must be large enough to hold figure of y=" + y);
        else if (startc + x >= ncols)
            throw new IllegalArgumentException("ncols must be large enough to hold figure of x=" + x);

        Grid grid = new Grid(ncols, nrows);
        int r = startr, c = startc;
        for (var rle : rleData) {
            if (rle.tag() == RleTag.EOL) {
                // set the rest of columns in the row to dead
                // and advance to next line
                while (c < grid.width) {
                    grid.set(r, c++, dead);
                }
                ++r;
                c = startc;
                continue;
            }

            // fill in columns based on rle data
            for (int i=0; i<rle.runCount(); ++i) {
                int val = rle.tag() == RleTag.ALIVE ? alive : dead;
                grid.set(r, c++, val);
            }
        }

        return new Pair<>(grid, new RleFile(file.getName().split("\\.")[0], ruleStr, x, y, birthConditions,
                surviveConditions, rleData));
    }

    /**
     * Extracts the digits from an RLE rule string
     * @param ruleStr An RLE rule string, for example: B3 or S345
     * @return the digits in the string
     */
    private static int[] extractDigits(String ruleStr) {
        if (ruleStr.charAt(0) != 'B' && ruleStr.charAt(0) != 'S')
            throw new IllegalArgumentException(String.format("Not a valid RLE rule string! '%s'", ruleStr));
        int[] digits = new int[ruleStr.length()-1];
        for (int i=1; i<ruleStr.length(); ++i)
            digits[i-1] = ruleStr.charAt(i) - '0';
        return digits;
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
                if (runCount.length() == 0) runCount.append("1");
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
