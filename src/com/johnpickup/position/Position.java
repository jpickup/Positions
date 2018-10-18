package com.johnpickup.position;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a position that comprises a number of component categories.
 * The net position is the sum of the individual category position.
 * Immutable - applyDelta returns a new Position with the delta applied.
 */
@EqualsAndHashCode
@ToString
public class Position {
    private final Map<PositionCategory, BigDecimal> components;

    public Position() {
        components = Collections.emptyMap();
    }

    public Position(PositionCategory category, BigDecimal position) {
        components = Collections.singletonMap(category, position);
    }

    private Position(Position source, PositionCategory category, BigDecimal position) {
        Map<PositionCategory, BigDecimal> newComponents = new HashMap<>(source.components);
        newComponents.put(category, position);
        components = Collections.unmodifiableMap(newComponents);
    }

    public BigDecimal getNetPosition() {
        return components.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Position applyDelta(PositionCategory category, BigDecimal delta) {
        return new Position(this, category, getPositionComponent(category).add(delta));
    }

    public BigDecimal getPositionComponent(PositionCategory category) {
        return components.getOrDefault(category, BigDecimal.ZERO);

    }
}
