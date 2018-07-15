package org.uatransport.service.converter.model;

public enum QuantityLoadFeedback {
 SIT(10), STAY(5), CROWDING(2), MISS(1),
    UNPAID_TRAVEL(1), THEFT(1), GRANDMA_QUARREL(1),
    WINDOW_QUARREL(1), DRUNK(1), WITHOUT_CHANGE(1) ;

    private final int rate;

    QuantityLoadFeedback(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }
}
