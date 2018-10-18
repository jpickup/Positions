package com.johnpickup.position;

import com.johnpickup.data.Book;
import com.johnpickup.data.Instrument;
import com.johnpickup.trade.BondTrade;
import com.johnpickup.trade.RepoTrade;
import com.johnpickup.trade.TradeUtils;

import java.time.LocalDate;

/**
 * Manages position by maintaining a cache and allowing updates to the cache in the form of trades.
 * When a trade amendment takes place, the previous version of the trade should be un-applied
 * and the new version applied.
 */
public class PositionManager {
    private final PositionCache positionCache = new PositionCache();

    public void applyBondTrade(BondTrade bondTrade) {
        positionCache.applyDelta(new PositionKey(bondTrade.getBook(), bondTrade.getInstrument()),
                PositionCategory.BOND,
                bondTrade.getSettlementDate(), TradeUtils.signedQuantity(bondTrade));
    }

    public void unapplyBondTrade(BondTrade bondTrade) {
        positionCache.applyDelta(new PositionKey(bondTrade.getBook(), bondTrade.getInstrument()),
                PositionCategory.BOND,
                bondTrade.getSettlementDate(), TradeUtils.signedQuantity(bondTrade).negate());
    }


    public void applyRepoTrade(RepoTrade repoTrade) {
        positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                PositionCategory.REPO,
                repoTrade.getStartDate(), TradeUtils.signedStartQuantity(repoTrade));

        if (repoTrade.getEndDate() != null) {
            positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                    PositionCategory.REPO,
                    repoTrade.getEndDate(), TradeUtils.signedEndQuantity(repoTrade));
        }
    }

    public void unapplyRepoTrade(RepoTrade repoTrade) {
        positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                PositionCategory.REPO,
                repoTrade.getStartDate(), TradeUtils.signedStartQuantity(repoTrade).negate());

        if (repoTrade.getEndDate() != null) {
            positionCache.applyDelta(new PositionKey(repoTrade.getBook(), repoTrade.getInstrument()),
                    PositionCategory.REPO,
                    repoTrade.getEndDate(), TradeUtils.signedEndQuantity(repoTrade).negate());
        }
    }

    public Position getPosition(Book book, Instrument instrument, LocalDate date) {
        return positionCache.getPosition(new PositionKey(book, instrument), date);
    }

}
