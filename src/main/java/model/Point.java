package model;

/**
 * Created by Demishev on 29.04.14.
 */
public class Point {
    private final double x;
    private final double y;
    private final boolean isCaptured;

    public Point(double x, double y, boolean isCaptured) {
        this.x = x;
        this.y = y;
        this.isCaptured = isCaptured;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isCaptured() {
        return isCaptured;
    }
}
