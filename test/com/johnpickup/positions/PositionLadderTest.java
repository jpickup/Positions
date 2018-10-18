package com.johnpickup.positions;

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
        Position expected = new Position(BigDecimal.ZERO);
        assertEquals(expected, actual);
    }

    @Test
    void createPosition() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void updatePosition() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected = new Position(BigDecimal.valueOf(3000));
        assertEquals(expected, actual);
    }

    @Test
    void createPositionExistsInFuture() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        Position actual = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        Position expected = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void createPositionStep() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 20), BigDecimal.valueOf(1000));

        Position actual19 = positionLadder.getPosition(LocalDate.of(2018, 10, 19));
        Position expected19 = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected19, actual19);

        Position actual20 = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        Position expected20 = new Position(BigDecimal.valueOf(2000));
        assertEquals(expected20, actual20);
    }

    @Test
    void createMiddlePositionStep() {
        positionLadder.applyDelta(LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 20), BigDecimal.valueOf(1000));
        positionLadder.applyDelta(LocalDate.of(2018, 10, 19), BigDecimal.valueOf(1000));

        Position actual18 = positionLadder.getPosition(LocalDate.of(2018, 10, 18));
        Position expected18 = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected18, actual18);

        Position actual19 = positionLadder.getPosition(LocalDate.of(2018, 10, 19));
        Position expected19 = new Position(BigDecimal.valueOf(2000));
        assertEquals(expected19, actual19);

        Position actual20 = positionLadder.getPosition(LocalDate.of(2018, 10, 20));
        Position expected20 = new Position(BigDecimal.valueOf(3000));
        assertEquals(expected20, actual20);
    }

}