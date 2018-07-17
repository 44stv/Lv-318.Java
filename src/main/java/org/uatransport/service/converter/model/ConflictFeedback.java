package org.uatransport.service.converter.model;

public enum ConflictFeedback {
    UNPAID_TRAVEL(10), THEFT(1), GRANDMA_QUARREL(1),
    WINDOW_QUARREL(3), DRUNK(3), WITHOUT_CHANGE(2), NO_CONFLICT(10);

    private final int rate;

    ConflictFeedback(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }
}
