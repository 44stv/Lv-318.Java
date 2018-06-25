package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.TransitDTO;

@Component
@RequiredArgsConstructor
public class TransitMap implements Converter<Transit, TransitDTO> {

    @Override
    public TransitDTO convert(MappingContext<Transit, TransitDTO> mappingContext) {
        Transit source = mappingContext.getSource();
        TransitDTO destination = mappingContext.getDestination();
        // List<Stop> stops = source.getStops();

        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setCategoryId(source.getCategory().getId());

        // if (!stops.isEmpty()) {
        // destination.setRouteName(stops.get(0).getStreet() + " - " + stops.get(stops.size() - 1).getStreet());
        // } else {
        // destination.setRouteName("Empty");
        // }

        return destination;
    }
}
