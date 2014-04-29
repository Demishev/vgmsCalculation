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
    private final int resolution;
    private final Function function;

    private Random random = new Random();

    public Calculator(int resolution, Function function) {
        this.resolution = resolution;
        this.function = function;
    }

    public CapturedArea calculate() {
        double xCenter = 0;
        double yCenter = 0;
        double scale = SMALLEST_RANGE;


        while (!function.isCaptured(xCenter, yCenter)) {
            xCenter = (random.nextDouble() - 0.5) * SEARCH_RANGE;
            yCenter = (random.nextDouble() - 0.5) * SEARCH_RANGE;
        }


        while (isCapturedOnBound(scale, xCenter, yCenter)) {
            scale *= 2;
        }

        List<Point> points = new ArrayList<Point>(resolution * resolution);


        for (int i = 0; i < resolution * resolution * 25; i++) {
            double currentX = xCenter + 2 * scale * (random.nextDouble() - 0.5);
            double currentY = yCenter + 2 * scale * (random.nextDouble() - 0.5);

            points.add(new Point(currentX, currentY, function.isCaptured(currentX, currentY)));
        }

        return new CapturedArea(points, xCenter, yCenter, scale);
    }

    private boolean isCapturedOnBound(double scale, double xCenter, double yCenter) {
        for (int i = 0; i < 100; i++) {
            double delta = random.nextDouble() + scale;
            if (function.isCaptured(xCenter - scale, yCenter - scale + delta)) {
                return true;
            }
            if (function.isCaptured(xCenter + scale, yCenter - scale + delta)) {
                return true;
            }
            if (function.isCaptured(xCenter - scale + delta, yCenter - scale)) {
                return true;
            }
            if (function.isCaptured(xCenter - scale + delta, yCenter + scale)) {
                return true;
            }
        }
        return false;
    }

}
