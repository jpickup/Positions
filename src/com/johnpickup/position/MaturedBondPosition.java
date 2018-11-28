package com.johnpickup.position;

import java.math.BigDecimal;

/**
 * Specialisation of position that can't be changed (applyDelta does nothing)
 * Should exist at the end of a position ladder on the maturity date of the bond
 */
public class MaturedBondPosition extends Position {
    public MaturedBondPosition() {
        super();
    }

    @Override
    public Position applyDelta(PositionCategory category, BigDecimal delta) {
        return this;
    }

}
