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
public class TransitMapper implements Converter<Transit, TransitDTO> {

    @Override
    public TransitDTO convert(MappingContext<Transit, TransitDTO> mappingContext) {
        Transit source = mappingContext.getSource();
        TransitDTO destination = mappingContext.getDestination();
        List<Stop> stops = source.getStops();

        String firstStop;
        String lastStop = "";
        boolean found = false;

        // TODO: rewrite search
        if (!stops.isEmpty()) {
            firstStop = stops.get(0).getStreet();
            for (int i = 0; i < stops.size() - 1; i++) {
                if (exists(stops, i)) {
                    lastStop = stops.get(i).getStreet();
                    found = true;
                }
                if (found) {
                    break;
                }
            }
            if (!found) {
                lastStop = stops.get(stops.size() / 2).getStreet();
            }
            destination.setRouteName(firstStop + " - " + lastStop);
        } else {
            destination.setRouteName("Empty");
        }

        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setCategoryId(source.getCategory().getId());
        destination.setCategoryIconURL(source.getCategory().getIconURL());
        destination.setStops(source.getStops());
        return destination;
    }

    private boolean exists(List<Stop> stops, int i) {
        return stops.get(i).getStreet().equals(stops.get(i + 1).getStreet())
                && !(stops.get(i).getDirection().equals(stops.get(i + 1).getDirection()));
    }
}
