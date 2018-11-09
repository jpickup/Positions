package com.johnpickup.position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A collection of position ladders keyed by book+instrument
 */
public class PositionCache {
    private final Map<PositionKey, PositionLadder> positions = new HashMap<>();
    private final List<PositionSubscriber> subscribers = new CopyOnWriteArrayList<>();

    private PositionLadder getPositionLadder(PositionKey key) {
        positions.putIfAbsent(key, new PositionLadder(subscribers));
        return positions.get(key);
    }

    public Position getPosition(PositionKey key, LocalDate date) {
        return getPositionLadder(key).getPosition(date);
    }

    public void applyDelta(PositionKey key, PositionCategory category, LocalDate date, BigDecimal delta) {
        getPositionLadder(key).applyDelta(date, category, delta);
    }

    public void subscribe(PositionSubscriber subscriber) {
        subscribers.add(subscriber);
    }
}
