package org.uatransport.service.converter.model;

public enum QuantityLoadFeedback {
    SIT(10), STAY(5), HARD_LOAD(2), LOSER(0);

    private final int rate;

    QuantityLoadFeedback(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }
}
