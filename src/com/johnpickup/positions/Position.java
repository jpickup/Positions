package com.johnpickup.positions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Position {
    private final BigDecimal position;
    // further position breakdown could live here...

    public Position applyDelta(BigDecimal delta) {
        return new Position(position.add(delta));
    }
}
