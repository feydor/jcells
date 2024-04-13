package org.fffere.jcell.view;

import org.fffere.jcell.rule.StateRule;

public class Rule2ColorMapper {
    /**
     * Returns an array of RGB values that are a gradient between dead and alive
     */
    public static int[] mapColorsFromRule(StateRule rule, int defaultDeadColor, int defaultAliveColor) {
        return colorGradient(defaultDeadColor, defaultAliveColor, rule.getNumStates());
    }

    private static int[] colorGradient(int baseDeadColor, int baseAliveColor, int steps) {
        int[] start = rgbFrom32bitInt(baseDeadColor);
        int[] dest = rgbFrom32bitInt(baseAliveColor);

        int[] diff = rgbDiff(dest, start, steps);

        int[] gradient = new int[steps];
        for (int i=0; i<steps; ++i) {
            start[0] += (i * diff[0]);
            start[1] += (i * diff[1]);
            start[2] += (i * diff[2]);
            gradient[i] = int32FromRgb(start);
        }

        return gradient;
    }

    private static int[] rgbFrom32bitInt(int n) {
        return new int[]{red(n), green(n), blue(n)};
    }

    private static int int32FromRgb(int[] rgb) {
        return ((rgb[0] << 16) & 0xff0000) | ((rgb[1] << 8) & 0xff00) | (rgb[2] & 0xff);
    }

    private static int[] rgbDiff(int[] rgb1, int[] rgb2, int steps) {
        int[] res = new int[3];
        for (int i=0; i<3; ++i)
            res[i] = (rgb1[i] - rgb2[i]) / (steps - 1);
        return res;
    }

    private static int red(int rgb) {
        return (rgb >> 16) & 0xff;
    }

    private static int green(int rgb) {
        return (rgb >> 8) & 0xff;
    }

    private static int blue(int rgb) {
        return rgb & 0xff;
    }

    private Rule2ColorMapper(){}
}
