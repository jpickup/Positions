package com.johnpickup.positions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void applyDelta() {
        Position p = new Position(new BigDecimal(100));
        Position actual = p.applyDelta(new BigDecimal(200));
        Position expected = new Position(new BigDecimal(300));
        assertEquals(expected, actual);
    }

    @Test
    void applyNegativeDelta() {
        Position p = new Position(new BigDecimal(100));
        Position actual = p.applyDelta(new BigDecimal(-200));
        Position expected = new Position(new BigDecimal(-100));
        assertEquals(expected, actual);
    }
}