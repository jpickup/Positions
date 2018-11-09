package com.johnpickup.position;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Representation of positions over a series of dates. Applying a delta will create a new date entry if none exists
 * and update any future dated positions.
 */
@RequiredArgsConstructor
public class PositionLadder {
    private final TreeMap<LocalDate, Position> positions = new TreeMap<>();
    private final List<PositionSubscriber> subscribers;

    public Position getPosition(LocalDate date) {
        Map.Entry<LocalDate, Position> positionEntry = positions.floorEntry(date);
        return positionEntry==null?new Position():positionEntry.getValue();
    }

    public void applyDelta(LocalDate date, PositionCategory category, BigDecimal delta) {
        positions.put(date, applyDeltaToPosition(getPosition(date), category, delta));
        NavigableMap<LocalDate, Position> futurePositions = positions.tailMap(date, false);
        futurePositions.forEach((localDate, position) ->
                futurePositions.replace(localDate, applyDeltaToPosition(position, category, delta)));
    }

    private Position applyDeltaToPosition(Position position, PositionCategory category, BigDecimal delta) {
        Position newPosition = position.applyDelta(category, delta);
        notifySubscribers(newPosition);
        return newPosition;
    }

    private void notifySubscribers(Position position) {
        PositionNotification positionNotification = new PositionNotification(position);
        subscribers.stream().forEach(s -> s.update(positionNotification));
    }
}
