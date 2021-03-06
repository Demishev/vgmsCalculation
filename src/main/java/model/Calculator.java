package model;

import model.physicalModels.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Calculator {
    public static final int SEARCH_RANGE = 100;
    public static final double SMALLEST_RANGE = 10E-2;
    public static final int DEFAULT_RESOLUTION = 2000;
    private final int resolution;
    private final Function function;

    private Random random = new Random();

    public Calculator(int resolution, Function function) {
        this.resolution = resolution;
        this.function = function;
    }

    public Calculator(Function function) {
        this.function = function;

        resolution = DEFAULT_RESOLUTION;
    }

    public ResultSet calculate(double initialZ) {
        double xCenter = 0;
        double yCenter = 0;
        double scale = SMALLEST_RANGE;


        while (!function.isCaptured(xCenter, yCenter, initialZ)) {
            xCenter = (random.nextDouble() - 0.5) * SEARCH_RANGE;
            yCenter = (random.nextDouble() - 0.5) * SEARCH_RANGE;
        }

        while (isCapturedOnBound(scale, xCenter, yCenter, initialZ)) {
            scale *= 2;
        }
        List<Point> points = new ArrayList<Point>(resolution * resolution);


        for (int i = 0; i < resolution * DEFAULT_RESOLUTION; i++) {
            double currentX = xCenter + 2 * scale * (random.nextDouble() - 0.5);
            double currentY = yCenter + 2 * scale * (random.nextDouble() - 0.5);

            final Point point = new Point(currentX, currentY, function.isCaptured(currentX, currentY, 0));
            points.add(point);
        }

        return new ResultSet(points, xCenter, yCenter, scale);
    }

    private boolean isCapturedOnBound(double scale, double xCenter, double yCenter, double initialZ) {
        for (int i = 0; i < DEFAULT_RESOLUTION; i++) {
            double delta = random.nextDouble() + scale;
            if (function.isCaptured(xCenter - scale, yCenter - scale + delta, initialZ)) {
                return true;
            }
            if (function.isCaptured(xCenter + scale, yCenter - scale + delta, initialZ)) {
                return true;
            }
            if (function.isCaptured(xCenter - scale + delta, yCenter - scale, initialZ)) {
                return true;
            }
            if (function.isCaptured(xCenter - scale + delta, yCenter + scale, initialZ)) {
                return true;
            }
        }
        return false;
    }

    public ResultSet getTrajectory(double startX, double startY, double startZ) {
        List<Point> points = new ArrayList<>();

        Point currentPoint = new Point(startX, startY, startZ);
        points.add(currentPoint);

        double minX = startX;
        double maxX = startX;

        double minZ = startZ;
        double maxZ = startZ;

        while (function.canGoOn(currentPoint.getX(), currentPoint.getY(), currentPoint.getZ())) {
            currentPoint = function.getNextPoint(currentPoint.getX(), currentPoint.getY(), currentPoint.getZ());
            points.add(currentPoint);

            if (currentPoint.getX() < minX) {
                minX = currentPoint.getX();
            }
            if (currentPoint.getX() > maxX) {
                maxX = currentPoint.getX();
            }
            if (currentPoint.getZ() < minZ) {
                minZ = currentPoint.getZ();
            }
            if (currentPoint.getZ() > maxZ) {
                maxZ = currentPoint.getZ();
            }
        }

        final ResultSet result = new ResultSet(points, minX, maxX, minZ, maxZ);
        System.out.println(result);
        return result;
    }
}
