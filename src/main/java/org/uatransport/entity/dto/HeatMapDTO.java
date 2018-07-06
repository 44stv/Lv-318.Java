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