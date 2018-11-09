package com.johnpickup.trade;

import com.johnpickup.data.Book;
import com.johnpickup.data.BuyOrSell;
import com.johnpickup.data.Instrument;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Data
public class RepoTrade {
    private final LocalDate startDate;
    private final Optional<LocalDate> endDate;
    private final Book book;
    private final Instrument instrument;
    private final BuyOrSell direction;
    private final BigDecimal quantity;

    public RepoTrade(LocalDate startDate, LocalDate endDate, Book book, Instrument instrument, BuyOrSell direction, BigDecimal quantity) {
        this.startDate = startDate;
        this.endDate = Optional.ofNullable(endDate);
        this.book = book;
        this.instrument = instrument;
        this.direction = direction;
        this.quantity = quantity;
    }
}
