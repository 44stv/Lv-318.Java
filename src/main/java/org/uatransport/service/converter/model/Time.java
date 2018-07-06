package org.uatransport.service.converter.model;

import lombok.Data;

@Data
public class Time {
    public static final Integer MINUTES_IN_HOUR = 60;
    private Integer hour;
    private Integer minute;

    Integer toMinutes() {
        return this.getHour() * MINUTES_IN_HOUR + this.getMinute();
    }
}
