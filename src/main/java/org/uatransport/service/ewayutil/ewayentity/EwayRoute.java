package org.uatransport.service.ewayutil.ewayentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EwayRoute implements Serializable {
    private Integer id;
    private String title;

    @JsonProperty("start_position")
    private Integer startPosition;
    @JsonProperty("stop_position")
    private Integer stop_position;
    private String transport;
}
