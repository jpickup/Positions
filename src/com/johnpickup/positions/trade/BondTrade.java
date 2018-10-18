package com.johnpickup.positions.trade;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.BuyOrSell;
import com.johnpickup.positions.data.Instrument;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Data
public class BondTrade {
    private final LocalDate settlementDate;
    private final Book book;
    private final Instrument instrument;
    private final BuyOrSell direction;
    private final BigDecimal quantity;
}
