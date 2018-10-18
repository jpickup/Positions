package com.johnpickup.position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Representation of positions over a series of dates. Applying a delta will create a new date entry if none exists
 * and update any future dated positions.
 */
public class PositionLadder {
    private final TreeMap<LocalDate, Position> positions = new TreeMap<>();

    public Position getPosition(LocalDate date) {
        Map.Entry<LocalDate, Position> positionEntry = positions.floorEntry(date);
        return positionEntry==null?new Position():positionEntry.getValue();
    }

    public void applyDelta(LocalDate date, PositionCategory category, BigDecimal delta) {
        positions.put(date, getPosition(date).applyDelta(category, delta));
        NavigableMap<LocalDate, Position> futurePositions = positions.tailMap(date, false);
        futurePositions.forEach((localDate, position) -> futurePositions.replace(localDate, position.applyDelta(category, delta)));
    }
}
