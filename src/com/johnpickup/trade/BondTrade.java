package com.johnpickup.trade;

import com.johnpickup.data.Book;
import com.johnpickup.data.BuyOrSell;
import com.johnpickup.data.Instrument;
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
