package model;

import java.util.List;

/**
 *  Created by Demishev on 29.04.14.
 */
public class CapturedArea {
    private final List<Point> points;

    private final double xCenter;
    private final double yCenter;

    private final double delta;

    public CapturedArea(List<Point> points, double xCenter, double yCenter, double delta) {
        this.points = points;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.delta = delta;
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
}
