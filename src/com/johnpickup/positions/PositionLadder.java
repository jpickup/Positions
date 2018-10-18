package com.johnpickup.positions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class PositionLadder {
    private final TreeMap<LocalDate, Position> positions = new TreeMap<>();

    public Position getPosition(LocalDate date) {
        Map.Entry<LocalDate, Position> positionEntry = positions.floorEntry(date);
        return positionEntry==null?new Position(BigDecimal.ZERO):positionEntry.getValue();
    }

    public void applyDelta(LocalDate date, BigDecimal delta) {
        positions.put(date, getPosition(date).applyDelta(delta));
        NavigableMap<LocalDate, Position> futurePositions = positions.tailMap(date, false);
        futurePositions.forEach((localDate, position) -> futurePositions.replace(localDate, position.applyDelta(delta)));
    }
}
