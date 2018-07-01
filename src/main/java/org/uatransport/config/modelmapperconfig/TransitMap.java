package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.TransitDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransitMap implements Converter<Transit, TransitDTO> {

    @Override
    public TransitDTO convert(MappingContext<Transit, TransitDTO> mappingContext) {
        Transit source = mappingContext.getSource();
        TransitDTO destination = mappingContext.getDestination();
        List<Stop> stops = source.getStops();

        String firstStop;
        String lastStop = "";
        boolean found = false;

        //TODO: rewrite search
        if (!stops.isEmpty()) {
            firstStop = stops.get(0).getStreet();
            for (int i = 0; i < stops.size(); i++) {
                if (stops.get(i).getStreet().equals(stops.get(i+1).getStreet())
                    && stops.get(i).getDirection() != stops.get(i+1).getDirection()) {
                    lastStop = stops.get(i).getStreet();
                    found = true;
                }
                if (found) break;
            }
            destination.setRouteName(firstStop + " - " + lastStop);
        } else {
            destination.setRouteName("Empty");
        }


        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setCategoryId(source.getCategory().getId());
        destination.setCategoryIconURL(source.getCategory().getIconURL());

        return destination;
    }
}
