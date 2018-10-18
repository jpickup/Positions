package com.johnpickup.position;

import com.johnpickup.data.Book;
import com.johnpickup.data.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionCacheTest {
    private PositionCache positionCache = new PositionCache();
    private PositionKey key1;
    private PositionKey key2;

    @BeforeEach
    void setUp() {
        positionCache = new PositionCache();
        key1 = new PositionKey(new Book("Book1"), new Instrument("Inst1"));
        key2 = new PositionKey(new Book("Book2"), new Instrument("Inst2"));
    }

    @Test
    void testEmpty() {
        Position actual = positionCache.getPosition(key1, LocalDate.of(2018, 10, 18));
        Position expected = new Position();
        assertEquals(expected, actual);
    }

    @Test
    void testCreate() {
        positionCache.applyDelta(key1, PositionCategory.BOND, LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionCache.applyDelta(key2, PositionCategory.BOND, LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));

        Position actual1 = positionCache.getPosition(key1, LocalDate.of(2018, 10, 18));
        Position actual2 = positionCache.getPosition(key2, LocalDate.of(2018, 10, 18));

        Position expected = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }

    @Test
    void testUpdate() {
        positionCache.applyDelta(key1, PositionCategory.BOND, LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionCache.applyDelta(key1, PositionCategory.BOND, LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));
        positionCache.applyDelta(key2, PositionCategory.BOND, LocalDate.of(2018, 10, 18), BigDecimal.valueOf(1000));

        Position actual1 = positionCache.getPosition(key1, LocalDate.of(2018, 10, 18));
        Position actual2 = positionCache.getPosition(key2, LocalDate.of(2018, 10, 18));

        Position expected1 = new Position(PositionCategory.BOND, BigDecimal.valueOf(2000));
        Position expected2 = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

}