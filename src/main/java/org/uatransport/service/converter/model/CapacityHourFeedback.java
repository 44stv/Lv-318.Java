package org.uatransport.service.converter.model;

import com.google.common.collect.Range;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.uatransport.service.converter.model.Time.MINUTES_IN_HOUR;

@Data
public class CapacityHourFeedback {

    private Integer capacity;
    private Time startTime;
    private Time endTime;

    public boolean containsHour(Integer hour) {
        Map<Boolean, List<Integer>> containsMinutesMap = IntStream.range(startTime.toMinutes(), endTime.toMinutes())
            .boxed()
            .collect(Collectors.partitioningBy(minute -> containMinute(hour, minute)));

        return containsMinutesMap.get(true).size() > containsMinutesMap.get(false).size();
    }

    private static boolean containMinute(Integer hour, Integer minute) {
        return Range.closed(hour * MINUTES_IN_HOUR, (hour + 1) * MINUTES_IN_HOUR).contains(minute);
    }
}
