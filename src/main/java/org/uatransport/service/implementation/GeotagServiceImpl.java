package org.uatransport.service.implementation;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Geotag;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.GeotadRepository;
import org.uatransport.service.GeotagService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeotagServiceImpl implements GeotagService {
    private final GeotadRepository geotadRepository;

    @Override
    public List<Geotag> getAllGeotags() {
        return Streams.stream(geotadRepository.findAll()).collect(Collectors.toList());
    }

    @Override
    public Geotag whereIAm(Double latitude, Double longtitude) {
        Map<Geotag, Double> distances = distancesToGeotags(latitude, longtitude);

        if (longtitude == null || latitude == null) {
            return geotadRepository.findById(1).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("Geotag with id '%s' not found", 1)));
        }

        Double minimalDistance = distances.values().stream().mapToDouble(v -> v).min()
                .orElseThrow(NoSuchElementException::new);

        for (Map.Entry<Geotag, Double> item : distances.entrySet()) {
            if (item.getValue().equals(minimalDistance)) {
                return item.getKey();
            }
        }

        throw new ResourceNotFoundException("Can`t resolve location");
    }

    @Override
    @Transactional
    public Geotag save(Geotag geotag) {
        if (geotag == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return geotadRepository.save(geotag);
    }

    /**
     * Method to calculate distances from given point to all available geotags.
     *
     * @param latitude
     *            latitude of current point
     * @param longtitude
     *            longtitude of current point
     * @return map, where keys are tags, values are distances from given point
     */
    private Map<Geotag, Double> distancesToGeotags(Double latitude, Double longtitude) {

        Map<Geotag, Double> mapOfDistances = new HashMap<>();

        Streams.stream(geotadRepository.findAll()).forEach(data -> {
            mapOfDistances.put(data, distance(data.getLatitude(), data.getLongtitude(), latitude, longtitude));
        });

        return mapOfDistances;
    }

    /**
     * Calculate distance between two points in latitude and longitude.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point
     *
     * @return Distance in Meters
     */
    private Double distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int earthRadius = 6371;

        Double latitudeDistance = Math.toRadians(lat2 - lat1);
        Double longtitudeDistance = Math.toRadians(lon2 - lon1);

        Double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(longtitudeDistance / 2) * Math.sin(longtitudeDistance / 2);

        Double atanValue = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double distance = earthRadius * atanValue * 1000; // convert to meters

        return Math.abs(distance);
    }
}
