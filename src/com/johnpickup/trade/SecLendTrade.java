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
public class SecLendTrade extends SecurityTrade {
    @Builder
    public SecLendTrade(TradeId tradeId, LocalDate startDate, LocalDate endDate, Book book, Instrument instrument,
                        BuyOrSell direction, BigDecimal quantity, LocalDateTime eventDateTime, TradeStatus tradeStatus) {
        super(tradeId, startDate, endDate, book, instrument, direction, quantity, eventDateTime, tradeStatus);
    }

    @Override
    public void applyPositionChange(PositionManager positionManager) {
        positionManager.applySecLendTrade(this);
    }

    @Override
    public void unapplyPositionChange(PositionManager positionManager) {
        positionManager.unapplySecLendTrade(this);
    }
}
