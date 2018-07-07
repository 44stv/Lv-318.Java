package org.uatransport.service;

import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopService {
    Stop save(Stop point);

    Stop getById(Integer id);

    void delete(Integer id);

    Stop update(Stop point);

    List<Stop> getByTransitId(Integer id);

    Stop getByTransitIdAndStopName(Integer transitId, String street);

    List<Stop> getByTransitIdAndDirection(Integer id, String direction);

    Integer getIndexByTransitIdAndStopName(Integer transitId, String street);

    List<Stop> getAll(Specification specification);
}
