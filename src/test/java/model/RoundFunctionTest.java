package model;

import model.physicalModels.RoundFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoundFunctionTest {
    private RoundFunction roundFunction;

    @Before
    public void setUp() throws Exception {
        roundFunction = new RoundFunction(1);
    }

    @Test
    public void shouldTrueWhenX0Y0() throws Exception {
        assertTrue(roundFunction.isCaptured(0, 0, 0));
    }

    @Test
    public void shouldFalseWhenX1Y1() throws Exception {
        assertFalse(roundFunction.isCaptured(1, 1, 0));
    }

    @Test
    public void shouldTrueWhenX1Y1Radius10() throws Exception {
        assertTrue(new RoundFunction(10).isCaptured(1, 1, 0));
    }

    @Test
    public void shouldTrueWhenX0_5Y0() throws Exception {
        assertTrue(roundFunction.isCaptured(0.5, 0, 0));
    }

    @Test
    public void shouldTrueWhenX0_25Y0_25() throws Exception {
        assertTrue(roundFunction.isCaptured(0.25, 0.25, 0));
    }

    @Test
    public void ShouldTrueWhenX5Y5Radius10() throws Exception {
        assertTrue(new RoundFunction(10).isCaptured(5, 5, 0));
    }

    @Test
    public void shouldNextPointWithSameXAndYButMinusPersent() throws Exception {
        assertEquals(99.9, roundFunction.getNextPoint(100, 2, 3).getX(), 0.0001);
        assertEquals(199.8, roundFunction.getNextPoint(1, 200, 3).getY(), 0.0001);
    }

    @Test
    public void shouldZBeMinus9_999WhenItWas10() throws Exception {
        assertEquals(-9.999, roundFunction.getNextPoint(1, 2, -10).getZ(), 0.0001);
    }

    @Test
    public void shouldZBeMinus4_999WhenItWas5() throws Exception {
        assertEquals(-4.999, roundFunction.getNextPoint(1, 2, -5).getZ(), 0.0001);
    }

    @Test
    public void shouldTrueWhenCanGoOnZIsMinus1() throws Exception {
        assertTrue(roundFunction.canGoOn(1, 2, -1));
    }

    @Test
    public void shouldFalseWhenCanGoOnZIs1() throws Exception {
        assertFalse(roundFunction.canGoOn(1, 2, 1));
    }
}
