package org.uatransport.service.ewayutil.ewayentity;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EwayRoute implements Serializable {
    private Integer id;
    private String title;

    @JsonProperty("start_position")
    private Integer startPosition;
    private Integer stop_position;
    private String transport;
}
