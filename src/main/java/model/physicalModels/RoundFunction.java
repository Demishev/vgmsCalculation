package model.physicalModels;

import model.Point;

/**
 * Created by Demishev on 29.04.14.
 */
public class RoundFunction implements Function {
    private final double radius;
    private final double xCenter;
    private final double yCenter;
    private final double zCenter = 0;

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
        return new Point(x * 0.999, y * 0.999, z + 0.001);
    }

    @Override
    public boolean canGoOn(double x, double y, double z) {
        return !(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2) + Math.pow(z - zCenter, 2) < Math.pow(radius, 2)) && z < 0;
    }

    @Override
    public double getBallRadius() {
        return radius;
    }
}
