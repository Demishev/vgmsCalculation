package model;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        Function roundFunction = new RoundFunction(10);

        calculator = new Calculator(100, roundFunction);
    }

    @Test
    public void should10000PointsWhenGetPicture() throws Exception {
        ResultSet result = calculator.calculate(0);

        assert result.getPoints().stream().filter(p -> p.getX() > result.getScale()).count() == 0;
        assert result.getPoints().stream().filter(p -> p.getX() < -result.getScale()).count() == 0;
        assert result.getPoints().stream().filter(p -> p.getY() > result.getScale()).count() == 0;
        assert result.getPoints().stream().filter(p -> p.getY() < -result.getScale()).count() == 0;

        assert result.getPoints().stream().filter(p -> Math.abs(p.getX()) < 5 && Math.abs(p.getY()) < 5 && !p.isCaptured()).count() == 0;
    }
}