package org.uatransport.service.converter.model;

public enum QuantityLoadFeedback {
    EMPTY_TRANSPORT(10), SIT(7), STAY(5), CROWDING(2), MISS(1), CRAZY_GRANDMA(-2);

    private final int rate;

    QuantityLoadFeedback(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }
}
