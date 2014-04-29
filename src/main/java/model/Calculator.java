package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Demishev on 29.04.14.
 */
public class Calculator {
    private final int resolution;
    private final Function function;
    private double scale = 0.000_000_1;
    private double xCenter = 0;
    private double yCenter = 0;
    private Random random = new Random();

    public Calculator(int resolution, Function function) {
        this.resolution = resolution;
        this.function = function;
    }

    public CapturedArea calculate() {
        while (!function.isCaptured(xCenter, yCenter)) {
            xCenter = (random.nextDouble() - 0.5) * 1000;
            yCenter = (random.nextDouble() - 0.5) * 1000;
        }

        while (isCapturedOnBound()) {
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

    private boolean isCapturedOnBound() {
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
