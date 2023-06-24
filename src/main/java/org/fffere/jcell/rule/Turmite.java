package org.fffere.jcell.rule;

import org.fffere.jcell.util.Coordinate;

public final class Turmite {
    private int row;
    private int col;
    private int angle;
    private final int color;
    private Coordinate bounds;

    public Turmite(int color, int angle, int row, int col) {
        this.row = row;
        this.col = col;
        this.angle = angle;
        this.color = color;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public int color() {
        return color;
    }

    public Coordinate bounds() {
        return bounds;
    }

    public void setBounds(Coordinate bounds) {
        this.bounds = bounds;
    }

    public void turnClockwise(int degrees) {
        turn(degrees, true);
    }

    public void turnCounterClockwise(int degrees) {
        turn(degrees, false);
    }

    private void turn(int degrees, boolean clockwise) {
        if (degrees % 90 != 0)
            throw new IllegalArgumentException("Can only turn in multiples of 90 degrees! degrees=" + degrees);
        angle = Math.abs(clockwise ? angle + degrees : angle - degrees) % 360;
    }

    public void moveForward() {
        int dir = angle / 90;
        switch (dir) {
            case 0 -> ++row;
            case 1 -> ++col;
            case 2 -> --row;
            case 3 -> --col;
            default -> throw new IllegalStateException("Angle must be a multiple of 90 degrees! angle=" + angle);
        }
        
        if (bounds == null)
            return;

        // wraparound
        if (col < 0) {
            col = bounds.col()-1;
        } else if (col >= bounds.col()) {
            col = 0;
        }
        
        if (row < 0) {
            row = bounds.row()-1;
        } else if (row >= bounds.row()) {
            row = 0;
        }
    }
}
