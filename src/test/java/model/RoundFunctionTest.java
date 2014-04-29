package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoundFunctionTest {
    private RoundFunction roundFunction;

    @Before
    public void setUp() throws Exception {
        roundFunction = new RoundFunction(1);
    }

    @Test
    public void shouldTrueWhenX0Y0() throws Exception {
        assertTrue(roundFunction.isCaptured(0, 0));
    }

    @Test
    public void shouldFalseWhenX1Y1() throws Exception {
        assertFalse(roundFunction.isCaptured(1, 1));
    }

    @Test
    public void shouldTrueWhenX1Y1Radius10() throws Exception {
        assertTrue(new RoundFunction(10).isCaptured(1, 1));
    }

    @Test
    public void shouldTrueWhenX0_5Y0() throws Exception {
        assertTrue(roundFunction.isCaptured(0.5, 0));
    }

    @Test
    public void shouldTrueWhenX0_25Y0_25() throws Exception {
        assertTrue(roundFunction.isCaptured(0.25, 0.25));
    }

    @Test
    public void ShouldTrueWhenX5Y5Radius10() throws Exception {
        assertTrue(new RoundFunction(10).isCaptured(5, 5));
    }
}