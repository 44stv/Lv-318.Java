package org.uatransport.service.converter.model;

import com.google.common.collect.Range;
import lombok.Data;

import java.util.Objects;

@Data
public class CapacityHourFeedback {
    private final Integer QUANTITY_MINUTES_IN_HOUR = 60;
    private Integer capacity;
    private Time startTime;
    private Time endTime;

    public boolean containsHour(Integer hour) {
        return (Objects.equals(this.startTime.getHour(), this.endTime.getHour()))
                ? containsHourWithEqualsStartEndHours(hour) : containsHourWithDifferentStartEndHours(hour);
    }

    private boolean containsHourWithEqualsStartEndHours(Integer hour) {
        int minuteValue = this.startTime.getHour() * this.QUANTITY_MINUTES_IN_HOUR + this.startTime.getMinute();
        int startPositionToCheck = hour * this.QUANTITY_MINUTES_IN_HOUR;
        int endPositionToCheck = startPositionToCheck + 1;
        return Range.closed(startPositionToCheck, endPositionToCheck).contains(minuteValue);
    }

    private boolean containsHourWithDifferentStartEndHours(Integer hour) {
        int startPosition = this.startTime.getHour() * this.QUANTITY_MINUTES_IN_HOUR + this.startTime.getMinute();
        int endPosition = this.endTime.getHour() * this.QUANTITY_MINUTES_IN_HOUR + this.endTime.getMinute();
        int countCheckedMinutes = (endPosition - startPosition) / 2; // 50% of all minutes of feedback period
        int counter = 0;
        int startPositionToCheck = hour * this.QUANTITY_MINUTES_IN_HOUR;
        int endPositionToCheck = startPositionToCheck + 1;
        for (int minuteValue = startPosition; minuteValue < endPosition; minuteValue++) {
            if (Range.closed(startPositionToCheck, endPositionToCheck).contains(minuteValue)) {
                counter++;
            }
        }
        return counter >= countCheckedMinutes;
    }
}
