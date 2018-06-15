package org.uatransport.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class CapacityBusyHoursFeedback {

    @JsonProperty("capacity")
    public Integer capacity;

    @JsonProperty("startHour")
    public Integer startHour;

    @JsonProperty("startMinute")
    public Integer startMinute;

    @JsonProperty("endHour")
    public Integer endHour;

    @JsonProperty("endMinute")
    public Integer endMinute;

    public  boolean existsInTimeRange(Integer time) {
        return IntStream.rangeClosed(this.startHour, this.endHour)
                .boxed()
                .collect(Collectors.toList())
                .contains(time);
    }

}
