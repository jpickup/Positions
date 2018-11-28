package com.johnpickup.trade;

import com.johnpickup.data.Book;
import com.johnpickup.data.BuyOrSell;
import com.johnpickup.data.Instrument;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public abstract class SecurityTrade implements Trade {
    private final TradeId tradeId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Book book;
    private final Instrument instrument;
    private final BuyOrSell direction;
    private final BigDecimal quantity;
    private final LocalDateTime eventDateTime;
    private final TradeStatus status;

    public Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }
}
