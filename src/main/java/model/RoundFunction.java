package model;

/**
 *  Created by Demishev on 29.04.14.
 */
public class RoundFunction implements Function {
    private final double radius;
    private final double xCenter;
    private final double yCenter;

    public RoundFunction(double radius) {
        this.radius = radius;
        xCenter = 0;
        yCenter = 0;
    }

    public RoundFunction(double radius, double xCenter, double yCenter) {
        this.radius = radius;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    @Override
    public boolean isCaptured(double x, double y, double z) {
        return Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2) < Math.pow(radius, 2);
    }

    @Override
    public Point getNextPoint(double x, double y, double z) {
        return new Point(x, y, z + 0.1);
    }

    @Override
    public boolean canGoOn(double x, double y, double z) {
        return z < 0;
    }
}
