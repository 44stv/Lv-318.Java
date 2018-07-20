package org.uatransport.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class HeatMapDTO {

    private String name;
    private List<SeriesItem> series = new ArrayList<>();

    public void setName(int intHourName) {
        // 10 is the first two-digit number
        if (intHourName < 10) {
            this.name = "0" + intHourName + ":00";
        } else {
            this.name = intHourName + ":00";
        }
    }

    public void setSeries(Map<String, Double> series) {
        series.forEach((key, value) -> this.series.add(new SeriesItem(key, value)));
    }
}

@Data
@Accessors(chain = true)
class SeriesItem {
    private String name;
    private Double value;

    SeriesItem(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
