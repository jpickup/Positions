package com.johnpickup.positions;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.BuyOrSell;
import com.johnpickup.positions.data.Instrument;
import com.johnpickup.positions.trade.BondTrade;
import com.johnpickup.positions.trade.RepoTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionServerTest {
    private PositionServer positionServer;
    private Book book1 = new Book("Book1");
    private Instrument inst1 = new Instrument("Inst1");

    @BeforeEach
    void setUp() {
        positionServer = new PositionServer();
    }

    @Test
    void noPosition() {
        Position actual = positionServer.getPosition(book1, inst1, LocalDate.of(2018,10,18));
        Position expected = new Position(BigDecimal.ZERO);
        assertEquals(expected, actual);
    }


    @Test
    void applyBondTrade() {
        LocalDate date = LocalDate.of(2018,10,18);
        BondTrade trade = new BondTrade(date, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionServer.applyBondTrade(trade);
        Position actual = positionServer.getPosition(book1, inst1, date);
        Position expected = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void applyOpenRepoTrade() {
        LocalDate date = LocalDate.of(2018,10,18);
        RepoTrade trade = new RepoTrade(date, null, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionServer.applyRepoTrade(trade);
        Position actual = positionServer.getPosition(book1, inst1, date);
        Position expected = new Position(BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void applyTermRepoTrade() {
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        RepoTrade trade = new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionServer.applyRepoTrade(trade);
        Position actualStart = positionServer.getPosition(book1, inst1, startDate);
        Position actualBeforeEnd = positionServer.getPosition(book1, inst1, endDate.minusDays(1));
        Position actualEnd = positionServer.getPosition(book1, inst1, endDate);
        Position expectedStart = new Position(BigDecimal.valueOf(1000));
        Position expectedEnd = new Position(BigDecimal.ZERO);
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedStart, actualBeforeEnd);
        assertEquals(expectedEnd, actualEnd);
    }

    @Test
    void applyBondAndTermRepoTrade() {
        LocalDate bondDate = LocalDate.of(2018,10,19);
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        positionServer.applyBondTrade(new BondTrade(bondDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        positionServer.applyRepoTrade(new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        Position actualStart = positionServer.getPosition(book1, inst1, startDate);
        Position actualBond = positionServer.getPosition(book1, inst1, bondDate);
        Position actualEnd = positionServer.getPosition(book1, inst1, endDate);
        Position expectedStart = new Position(BigDecimal.valueOf(1000));
        Position expectedBond = new Position(BigDecimal.valueOf(2000));
        Position expectedEnd = new Position(BigDecimal.valueOf(1000));
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedBond, actualBond);
        assertEquals(expectedEnd, actualEnd);
    }

}