package org.uatransport.service;

import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopService {

    Stop getByCoordinatesAndDirection(Double lat, Double lng, Stop.Direction direction);

    Stop getByLatAndLngAndDirection(Double lat, Double lng, Stop.Direction direction);

    Stop save(Stop point);

    Stop getById(Integer id);

    void delete(Integer id);

    Stop update(Stop point);

    List<Stop> getByTransitId(Integer id);

    Stop getByTransitIdAndStopNameAndDirection(Integer transitId, String street, Stop.Direction direction);

    List<Stop> getByTransitIdAndDirection(Integer id, Stop.Direction direction);

    List<Stop> getAll(Specification specification);

    Integer getIndexByTransitIdAndStopNameAndDirection(Integer transitId, String street, Stop.Direction direction);

}
