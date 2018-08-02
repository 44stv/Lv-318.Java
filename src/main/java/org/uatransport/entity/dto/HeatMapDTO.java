package org.uatransport.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.uatransport.entity.Stop;

import java.util.Map;
import java.util.TreeMap;

@Data
@Accessors(chain = true)
public class HeatMapDTO {
    private Map<String, Double> stopCapacityMap;
    private Map<String, Double> hourCapacityMap;

    public HeatMapDTO(Map<Stop, Double> stopCapacityMap, Map<Integer, Double> hourCapacityMap) {
        this.stopCapacityMap = new TreeMap<>();
        this.hourCapacityMap = new TreeMap<>();

        stopCapacityMap.forEach((stop, value) -> this.stopCapacityMap.put(stop.getStreet(), value));
        hourCapacityMap.forEach((hour, value) -> this.hourCapacityMap.put(setProperHourName(hour),value));
    }

    private String setProperHourName(Integer name) {
        // 10 is the first two-digit number
        if (name < 10) {
            return  "0" + name + ":00";
        } else {
            return name + ":00";
        }
    }
}