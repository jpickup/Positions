package com.johnpickup.positions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void applyDelta() {
        Position p = new Position(PositionCategory.BOND, new BigDecimal(100));
        Position actual = p.applyDelta(PositionCategory.BOND, new BigDecimal(200));
        Position expected = new Position(PositionCategory.BOND, new BigDecimal(300));
        assertEquals(expected, actual);
    }

    @Test
    void applyComponents() {
        Position p = new Position(PositionCategory.BOND, new BigDecimal(100));
        Position actual = p.applyDelta(PositionCategory.REPO, new BigDecimal(-200));
        assertEquals(BigDecimal.valueOf(100), actual.getPositionComponent(PositionCategory.BOND));
        assertEquals(BigDecimal.valueOf(-200), actual.getPositionComponent(PositionCategory.REPO));
        assertEquals(BigDecimal.valueOf(-100), actual.getNetPosition());
    }

    @Test
    void applyNegativeDelta() {
        Position p = new Position(PositionCategory.BOND, new BigDecimal(100));
        Position actual = p.applyDelta(PositionCategory.BOND, new BigDecimal(-200));
        Position expected = new Position(PositionCategory.BOND, new BigDecimal(-100));
        assertEquals(expected, actual);
    }
}