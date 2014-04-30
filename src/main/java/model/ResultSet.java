package model;

import java.util.List;

/**
 *  Created by Demishev on 29.04.14.
 */
public class ResultSet {
    private final List<Point> points;

    private final double xCenter;
    private final double yCenter;

    private final double delta;//TODO remove it from here!

    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    public ResultSet(List<Point> points, double xCenter, double yCenter, double delta) {
        this.points = points;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.delta = delta;

        minX = 0;
        maxX = 0;
        minZ = 0;
        maxZ = 0;
    }

    public ResultSet(List<Point> points, double minX, double maxX, double minZ, double maxZ) {
        this.points = points;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;

        yCenter = 0;
        xCenter = 0;

        delta = 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public double getxCenter() {
        return xCenter;
    }

    public double getyCenter() {
        return yCenter;
    }

    public double getScale() {
        return delta;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }
}
