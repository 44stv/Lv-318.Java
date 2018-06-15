package org.uatransport.service.converter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.uatransport.entity.Feedback;
import org.uatransport.service.converter.ConversionStrategy;
import org.uatransport.service.model.CapacityBusyHoursFeedback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CapacityBusyHoursConversionStrategy implements ConversionStrategy<CapacityBusyHoursFeedback> {


    @Override
    @SneakyThrows
    public CapacityBusyHoursFeedback convert(Feedback feedback) {
        return new ObjectMapper().readValue(feedback.getAnswer(), CapacityBusyHoursFeedback.class);
    }

}
