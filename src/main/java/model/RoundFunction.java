package model;

/**
 * Created by konstantin  on 29.04.14.
 */
public class RoundFunction implements Function {
    private final double radius;

    public RoundFunction(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean isCaptured(double x, double y) {
        return x * x + y * y < radius * radius;
    }
}
