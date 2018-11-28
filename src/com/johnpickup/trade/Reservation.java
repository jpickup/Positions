package com.johnpickup.trade;

import com.johnpickup.data.Book;
import com.johnpickup.data.BuyOrSell;
import com.johnpickup.data.Instrument;
import com.johnpickup.position.PositionManager;
import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString(callSuper = true)
public class Reservation extends SecurityTrade {
    @Builder
    public Reservation(TradeId tradeId, LocalDate startDate, LocalDate endDate, Book book, Instrument instrument,
                       BuyOrSell direction, BigDecimal quantity, LocalDateTime eventDateTime, TradeStatus status) {
        super(tradeId, startDate, endDate, book, instrument, direction, quantity, eventDateTime, status);
    }

    @Override
    public void applyPositionChange(PositionManager positionManager) {
        positionManager.applyReservation(this);
    }

    @Override
    public void unapplyPositionChange(PositionManager positionManager) {
        positionManager.unapplyReservation(this);
    }
}
