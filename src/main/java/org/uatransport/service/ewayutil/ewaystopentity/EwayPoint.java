package org.uatransport.service.ewayutil.ewaystopentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EwayPoint {
    private double lat;
    private double lng;
    private int direction;
    private String title;
}
