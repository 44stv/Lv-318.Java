package org.uatransport.service;

import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopService {

    boolean existByCoordinatesAndDirection(Double lat, Double lng, Stop.DIRECTION direction);

    Stop getByLatAndLngAndDirection(Double lat, Double lng, Stop.DIRECTION direction);

    Stop save(Stop point);

    Stop getById(Integer id);

    void delete(Integer id);

    Stop update(Stop point);

    List<Stop> getByTransitId(Integer id);

    Stop getByTransitIdAndStopNameAndDirection(Integer transitId, String street, String direction);

    List<Stop> getByTransitIdAndDirection(Integer id, String direction);

    List<Stop> getAll(Specification specification);

    // Integer getIndexByTransitIdAndStopName(Integer transitId, String street);

    Integer getIndexByTransitIdAndStopNameAndDirection(Integer transitId, String street, String direction);

}
