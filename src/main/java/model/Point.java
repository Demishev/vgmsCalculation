package model;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Point {
    private final double x;
    private final double y;
    private final double z;
    private final boolean isCaptured;

    public Point(double x, double y, boolean isCaptured) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.isCaptured = isCaptured;
    }

    public Point(double x, double y, double z) {
        this.z = z;
        this.y = y;
        this.x = x;
        isCaptured = false;
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

    public double getZ() {
        return z;
    }
}
