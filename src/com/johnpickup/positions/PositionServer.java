package com.johnpickup.positions;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.Instrument;
import com.johnpickup.positions.trade.BondTrade;
import com.johnpickup.positions.trade.RepoTrade;
import com.johnpickup.positions.trade.TradeUtils;

import java.time.LocalDate;

public class PositionServer {
    private final PositionCache positionCache = new PositionCache();

    public void applyBondTrade(BondTrade bondTrade) {
        positionCache.applyDelta(new PositionKey(bondTrade.getBook(), bondTrade.getInstrument()),
                bondTrade.getSettlementDate(), TradeUtils.signedQuantity(bondTrade));
    }

    public void applyRepoTrade(RepoTrade repoTrade) {
        positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                repoTrade.getStartDate(), TradeUtils.signedStartQuantity(repoTrade));

        if (repoTrade.getEndDate() != null) {
            positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                    repoTrade.getEndDate(), TradeUtils.signedEndQuantity(repoTrade));
        }
    }

    public Position getPosition(Book book, Instrument instrument, LocalDate date) {
        return positionCache.getPosition(new PositionKey(book, instrument), date);
    }

}
