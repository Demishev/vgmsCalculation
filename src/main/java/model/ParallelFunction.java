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

        while (!particleInBall(x, y, z) && !particleOutOfBounds(x, y, z)) {
            double reducedX = x / a;
            double reducedZ = z / a;
            double reducedR = Math.pow(x * x + y * y + z * z, 1 / 2) / a; //TODO И об этом не уверен
            double reducedH = hZ / mZ; //TODO Не уверен совсем.

            double reducedVX = reducedC * (reducedX / Math.pow(reducedR, 5)) * ((-4 * Math.PI / 3) * (4 * reducedZ * reducedZ / Math.pow(reducedR, 5) + 1 / Math.pow(reducedR, 3)) - 5 * reducedH * z * z / Math.pow(reducedR, 2) + reducedH);
            double reducedVY = reducedC * (reducedX / Math.pow(reducedR, 5)) * ((-4 * Math.PI / 3) * (4 * reducedZ * reducedZ / Math.pow(reducedR, 5) + 1 / Math.pow(reducedR, 3)) - 5 * reducedH * z * z / Math.pow(reducedR, 2) + reducedH);
            double reducedVZ = reducedC * (reducedZ / Math.pow(reducedR, 5)) * ((-4 * Math.PI / 3) * 4 * reducedZ * reducedZ / Math.pow(reducedR, 5) - 5 * reducedH * z * z / Math.pow(reducedR, 2) + 3 * reducedH) + 1; //TODO И по поводу +1 тоже вопрос

            x += liquidVelocity * reducedVX * delta;
            y += liquidVelocity * reducedVY * delta;
            z += liquidVelocity * reducedVZ * delta;
        }
        return particleInBall(x, y, z);
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
