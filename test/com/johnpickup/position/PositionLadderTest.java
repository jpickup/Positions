package com.johnpickup.position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionLadderTest {
    private PositionLadder positionLadder;

    @BeforeEach
    void setUp() {
        positionLadder = new PositionLadder();
    }

    @Test
    void nothingIsZero() {
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected = new Position();
        assertEquals(expected, actual);
    }

    @Test
    void createPosition() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void updatePosition() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.REPO, BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        assertEquals(BigDecimal.valueOf(3000), actual.getNetPosition());
        assertEquals(BigDecimal.valueOf(2000), actual.getPositionComponent(PositionCategory.BOND));
        assertEquals(BigDecimal.valueOf(1000), actual.getPositionComponent(PositionCategory.REPO));
    }

    @Test
    void createPositionExistsInFuture() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        assertEquals(BigDecimal.valueOf(1000), actual.getNetPosition());
    }

    @Test
    void createPositionStep() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 20), PositionCategory.BOND, BigDecimal.valueOf(1000));

        Position actual19 = positionLadder.getPosition(LocalDate.of(2018, 10, 19));
        Position expected19 = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected19, actual19);

        Position actual20 = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        Position expected20 = new Position(PositionCategory.BOND, BigDecimal.valueOf(2000));
        assertEquals(expected20, actual20);
    }

    @Test
    void createMiddlePositionStep() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), PositionCategory.BOND, BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 20), PositionCategory.BOND, BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 19), PositionCategory.BOND, BigDecimal.valueOf(1000));

        Position actual18 = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected18 = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected18, actual18);

        Position actual19 = positionLadder.getPosition(LocalDate.of(2018, 10, 19));
        Position expected19 = new Position(PositionCategory.BOND, BigDecimal.valueOf(2000));
        assertEquals(expected19, actual19);

        Position actual20 = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        Position expected20 = new Position(PositionCategory.BOND, BigDecimal.valueOf(3000));
        assertEquals(expected20, actual20);
    }

}