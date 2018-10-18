package com.johnpickup.positions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PositionCache {
    private final Map<PositionKey, PositionLadder> positions = new HashMap<>();

    private PositionLadder getPositionLadder(PositionKey key) {
        positions.putIfAbsent(key, new PositionLadder());
        return positions.get(key);
    }

    public Position getPosition(PositionKey key, LocalDate date) {
        return getPositionLadder(key).getPosition(date);
    }

    public void applyDelta(PositionKey key, LocalDate date, BigDecimal delta) {
        getPositionLadder(key).applyDelta(date, delta);
    }
}
