package model;

/**
 * Created by Demishev on 30.04.14.
 */
public class ParallelFunction implements Function {
    //Как я понимаю, случай работает тогда и только тогда, когда внешнее маг. поле направлено по z, жидкость - тоже.
    //Помимо того, по поводу намагничености тоже вопросы возникают...

    private final double χ;
    private final double η;

    private final double a;
    private final double b;

    private final double mX;
    private final double mY;
    private final double mZ;

    private final double hX;
    private final double hY;
    private final double hZ;

    private final double particleVolume;
    private final double liquidVelocity;

    public ParallelFunction(double χ, double η, double a, double b, double mX, double mY, double mZ, double hX, double hY, double hZ, double particleVolume, double liquidVelocity) {
        this.χ = χ;
        this.η = η;
        this.a = a;
        this.b = b;
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;
        this.hX = hX;
        this.hY = hY;
        this.hZ = hZ;
        this.particleVolume = particleVolume;
        this.liquidVelocity = liquidVelocity;
    }

    @Override
    public boolean isCaptured(double initialX, double initialY, double initialZ) {
        double x = initialX;
        double y = initialY;
        double z = initialZ;

        final double delta = b / liquidVelocity;
        final double reducedC = (2 * χ * particleVolume * vectorPower(mX, mY, mZ, 2)) / (3 * η * a * b * liquidVelocity);

        while (!particleInBall(x, y, z) && !particleOutOfBounds(x, y, z) && z <= 0 && x != Double.NaN && y != Double.NaN && z != Double.NaN) {
            final double reducedX = x / a;
            final double reducedY = y / a;
            final double reducedZ = z / a;
            final double reducedR = Math.pow(x * x + y * y + z * z, 1 / 2) / a;
            final double reducedH = hZ / mZ;

            final double rIn2 = Math.pow(reducedR, 2);
            final double rIn3 = Math.pow(reducedR, 3);
            final double rIn5 = Math.pow(reducedR, 5);
            final double zIn2 = Math.pow(reducedZ, 2);

            double reducedVX = reducedC * reducedX / rIn5 * (reducedH - 5 * reducedH * zIn2 / rIn2 - 4 * Math.PI / (3 * rIn3) - 4 * Math.PI * 4 * zIn2 / (3 * rIn5));
            double reducedVY = reducedC * reducedY / rIn5 * (reducedH - 5 * reducedH * zIn2 / rIn2 - 4 * Math.PI / (3 * rIn3) - 4 * Math.PI * 4 * zIn2 / (3 * rIn5));

            double reducedVZ = reducedC * (reducedZ / rIn5) * (3 * reducedH - 5 * reducedH * zIn2 / rIn2 - 4 * Math.PI * 4 * zIn2 / (3 * rIn5)) + 1;

            x += liquidVelocity * reducedVX * delta;
            y += liquidVelocity * reducedVY * delta;
            z += liquidVelocity * reducedVZ * delta;
        }
        return particleInBall(x, y, z);
    }

    @Override
    public Point getNextPoint(double x, double y, double z) {
        return null; //TODO code it
    }

    @Override
    public boolean canGoOn(double x, double y, double z) {
        return false; //TODO code this!
    }

    private double vectorPower(double x, double y, double z, int power) {
        return Math.pow(x, power) * Math.pow(y, power) * Math.pow(z, power);
    }


    private boolean particleOutOfBounds(double x, double y, double z) {
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) > 100 * Math.pow(a, 2);
    }

    private boolean particleInBall(double x, double y, double z) {
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) <= Math.pow(a, 2);
    }
}
