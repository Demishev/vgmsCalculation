package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Calculator {
    public static final int SEARCH_RANGE = 1000;
    public static final double SMALLEST_RANGE = 10E-5;
    public static final int INTITERATIONS = 25;
    private final int resolution;
    private final Function function;

    private Random random = new Random();

    public Calculator(int resolution, Function function) {
        this.resolution = resolution;
        this.function = function;
    }

    public CapturedArea calculate(double initialZ) {
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


        for (int i = 0; i < resolution * resolution * INTITERATIONS; i++) {
            double currentX = xCenter + 2 * scale * (random.nextDouble() - 0.5);
            double currentY = yCenter + 2 * scale * (random.nextDouble() - 0.5);

            points.add(new Point(currentX, currentY, function.isCaptured(currentX, currentY, 0)));

        }

        return new CapturedArea(points, xCenter, yCenter, scale);
    }

    private boolean isCapturedOnBound(double scale, double xCenter, double yCenter, double initialZ) {
        for (int i = 0; i < INTITERATIONS; i++) {
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

}
