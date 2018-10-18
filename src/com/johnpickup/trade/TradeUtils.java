package com.johnpickup.trade;

import java.math.BigDecimal;

public class TradeUtils {
    public static BigDecimal signedQuantity(BondTrade bondTrade) {
        switch (bondTrade.getDirection()) {
            case BUY:
                return bondTrade.getQuantity();
            case SELL:
                return bondTrade.getQuantity().negate();
            default:
                throw new RuntimeException("Unknown direction " + bondTrade.getDirection());
        }
    }

    public static BigDecimal signedStartQuantity(RepoTrade repoTrade) {
        switch (repoTrade.getDirection()) {
            case BUY:
                return repoTrade.getQuantity();
            case SELL:
                return repoTrade.getQuantity().negate();
            default:
                throw new RuntimeException("Unknown direction " + repoTrade.getDirection());
        }
    }

    public static BigDecimal signedEndQuantity(RepoTrade repoTrade) {
        return signedStartQuantity(repoTrade).negate();
    }
}
