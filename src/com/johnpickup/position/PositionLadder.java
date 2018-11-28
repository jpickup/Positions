package com.johnpickup.position;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * Representation of positions over a series of dates. Applying a delta will create a new date entry if none exists
 * and update any future dated positions.
 */
@RequiredArgsConstructor
public class PositionLadder {
    private final PositionKey positionKey;
    private final ConcurrentSkipListMap<LocalDate, Position> positions = new ConcurrentSkipListMap<>();
    private final InstrumentDetails instrumentDetails;
    private final List<PositionSubscriber> subscribers;
    private final Object lock = new Object();

    public Position getPosition(LocalDate date) {
        Map.Entry<LocalDate, Position> positionEntry = positions.floorEntry(date);
        return positionEntry == null ? new Position() : positionEntry.getValue();
    }

    public void setInitialCashPosition(LocalDate date, BigDecimal position) {
        synchronized (lock) {
            Position currentPosition = getPosition(date);
            BigDecimal delta = position.subtract(currentPosition.getPositionComponent(PositionCategory.BOND));
            applyDelta(date, PositionCategory.BOND, delta);
        }
    }

    public void applyDelta(LocalDate date, PositionCategory category, BigDecimal delta) {
        synchronized (lock) {
            positions.put(date, applyDeltaToPosition(date, getPosition(date), category, delta));
            NavigableMap<LocalDate, Position> futurePositions = positions.tailMap(date, false);
            futurePositions.forEach((localDate, position) ->
                    futurePositions.replace(localDate, applyDeltaToPosition(localDate, position, category, delta)));

            if (instrumentDetails != null && !instrumentDetails.isPerpetual() &&
                    !instrumentDetails.getMaturityDate().isBefore(date)) {
                positions.put(instrumentDetails.getMaturityDate(), new MaturedBondPosition());
            }
        }
    }

    private Position applyDeltaToPosition(LocalDate date, Position position, PositionCategory category, BigDecimal delta) {
        Position newPosition = position.applyDelta(category, delta);
        notifySubscribers(date, newPosition);
        return newPosition;
    }

    private void notifySubscribers(LocalDate date, Position position) {
        PositionNotification positionNotification = new PositionNotification(positionKey, date, position);
        subscribers.forEach(s -> s.update(positionNotification));
    }

    public Set<LocalDate> dates() {
        return positions.keySet();
    }

    public void broadcast() {
        positions.forEach(this::notifySubscribers);
    }

    public Collection<PositionNotification> asNotifications() {
        return positions.entrySet().stream()
                .map(entry -> new PositionNotification(positionKey, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
