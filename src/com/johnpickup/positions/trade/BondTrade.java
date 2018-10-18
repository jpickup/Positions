package com.johnpickup.positions.trade;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.BuyOrSell;
import com.johnpickup.positions.data.Instrument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class BondTrade {
    private final LocalDate settlementDate;
    private final Book book;
    private final Instrument instrument;
    private final BuyOrSell direction;
    private final BigDecimal quantity;
}
