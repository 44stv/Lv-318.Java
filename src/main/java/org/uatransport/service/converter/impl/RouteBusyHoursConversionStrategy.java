package org.uatransport.service.converter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.uatransport.entity.Feedback;
import org.uatransport.service.converter.ConversionStrategy;
import org.uatransport.service.model.RouteBusyHoursFeedback;

public class RouteBusyHoursConversionStrategy implements ConversionStrategy<RouteBusyHoursFeedback> {

    @Override
    @SneakyThrows
    public RouteBusyHoursFeedback convert(Feedback feedback) {
        return new ObjectMapper().readValue(feedback.getAnswer(), RouteBusyHoursFeedback.class);
    }

}
