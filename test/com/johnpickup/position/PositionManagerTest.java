package com.johnpickup.position;

import com.johnpickup.data.Book;
import com.johnpickup.data.BuyOrSell;
import com.johnpickup.data.Instrument;
import com.johnpickup.trade.BondTrade;
import com.johnpickup.trade.RepoTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionManagerTest {
    private PositionManager positionManager;
    private Book book1 = new Book("Book1");
    private Instrument inst1 = new Instrument("Inst1");

    @BeforeEach
    void setUp() {
        positionManager = new PositionManager();
    }

    @Test
    void noPosition() {
        Position actual = positionManager.getPosition(book1, inst1, LocalDate.of(2018,10,18));
        Position expected = new Position();
        assertEquals(expected, actual);
    }

    @Test
    void applyBondTrade() {
        LocalDate date = LocalDate.of(2018,10,18);
        BondTrade trade = new BondTrade(date, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionManager.applyBondTrade(trade);
        Position actual = positionManager.getPosition(book1, inst1, date);
        Position expected = new Position(PositionCategory.BOND, BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void unapplyBondTrade() {
        LocalDate date = LocalDate.of(2018,10,18);
        BondTrade trade = new BondTrade(date, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionManager.applyBondTrade(trade);
        positionManager.unapplyBondTrade(trade);
        Position actual = positionManager.getPosition(book1, inst1, date);
        Position expected = new Position(PositionCategory.BOND, BigDecimal.ZERO);
        assertEquals(expected, actual);
    }

    @Test
    void applyOpenRepoTrade() {
        LocalDate date = LocalDate.of(2018,10,18);
        RepoTrade trade = new RepoTrade(date, null, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionManager.applyRepoTrade(trade);
        Position actual = positionManager.getPosition(book1, inst1, date);
        Position expected = new Position(PositionCategory.REPO, BigDecimal.valueOf(1000));
        assertEquals(expected, actual);
    }

    @Test
    void applyTermRepoTrade() {
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        RepoTrade trade = new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionManager.applyRepoTrade(trade);
        Position actualStart = positionManager.getPosition(book1, inst1, startDate);
        Position actualBeforeEnd = positionManager.getPosition(book1, inst1, endDate.minusDays(1));
        Position actualEnd = positionManager.getPosition(book1, inst1, endDate);
        Position expectedStart = new Position(PositionCategory.REPO, BigDecimal.valueOf(1000));
        Position expectedEnd = new Position(PositionCategory.REPO, BigDecimal.ZERO);
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedStart, actualBeforeEnd);
        assertEquals(expectedEnd, actualEnd);
    }

    @Test
    void unapplyTermRepoTrade() {
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        RepoTrade trade = new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000));
        positionManager.applyRepoTrade(trade);
        positionManager.unapplyRepoTrade(trade);
        Position actualStart = positionManager.getPosition(book1, inst1, startDate);
        Position actualBeforeEnd = positionManager.getPosition(book1, inst1, endDate.minusDays(1));
        Position actualEnd = positionManager.getPosition(book1, inst1, endDate);
        Position expectedStart = new Position(PositionCategory.REPO, BigDecimal.ZERO);
        Position expectedEnd = new Position(PositionCategory.REPO, BigDecimal.ZERO);
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedStart, actualBeforeEnd);
        assertEquals(expectedEnd, actualEnd);
    }

    @Test
    void applyBondAndTermRepoTrade() {
        LocalDate bondDate = LocalDate.of(2018,10,19);
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        positionManager.applyBondTrade(new BondTrade(bondDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        positionManager.applyRepoTrade(new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        Position actualStart = positionManager.getPosition(book1, inst1, startDate);
        Position actualBond = positionManager.getPosition(book1, inst1, bondDate);
        Position actualEnd = positionManager.getPosition(book1, inst1, endDate);
        assertEquals(BigDecimal.valueOf(1000), actualStart.getNetPosition());
        assertEquals(BigDecimal.valueOf(2000), actualBond.getNetPosition());
        assertEquals(BigDecimal.valueOf(1000), actualEnd.getNetPosition());
    }

    @Test
    void applyBondAndTermRepoTradeThenUnapplyBond() {
        LocalDate bondDate = LocalDate.of(2018,10,19);
        LocalDate startDate = LocalDate.of(2018,10,18);
        LocalDate endDate = LocalDate.of(2018,10,25);
        positionManager.applyBondTrade(new BondTrade(bondDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        positionManager.applyRepoTrade(new RepoTrade(startDate, endDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        positionManager.unapplyBondTrade(new BondTrade(bondDate, book1, inst1, BuyOrSell.BUY, BigDecimal.valueOf(1000)));
        Position actualStart = positionManager.getPosition(book1, inst1, startDate);
        Position actualBond = positionManager.getPosition(book1, inst1, bondDate);
        Position actualEnd = positionManager.getPosition(book1, inst1, endDate);
        assertEquals(BigDecimal.valueOf(1000), actualStart.getNetPosition());
        assertEquals(BigDecimal.valueOf(1000), actualBond.getNetPosition());
        assertEquals(BigDecimal.ZERO, actualEnd.getNetPosition());
    }

}