package org.uatransport.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.uatransport.entity.Stop;

import java.time.LocalDateTime;

public class RouteBusyHoursFeedback {

    @JsonProperty("from")
    public String from;

    @JsonProperty("to")
    public String to;

    @JsonProperty("startHour")
    public Integer startHour;

    @JsonProperty("startMinute")
    public Integer startMinute;

    @JsonProperty("endHour")
    public Integer endHour;

    @JsonProperty("endMinute")
    public Integer endMinute;

}
