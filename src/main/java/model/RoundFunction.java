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
    public boolean isCaptured(double x, double y) {
        return Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2) < Math.pow(radius, 2);
    }
}
