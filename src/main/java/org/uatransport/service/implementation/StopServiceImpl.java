package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Stop;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.StopRepository;
import org.uatransport.service.StopService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {

    private final StopRepository stopRepository;

    @Override
    public Stop getByCoordinatesAndDirection(Double lat, Double lng, Stop.Direction direction) {
        return stopRepository.findByLatAndLngAndDirection(lat, lng, direction);
    }

    @Override
    public Stop getByLatAndLngAndDirection(Double lat, Double lng, Stop.Direction direction) {
        return stopRepository.findByLatAndLngAndDirection(lat, lng, direction);
    }

    @Override
    @Transactional
    public Stop save(Stop point) {
        if (point == null) {
            throw new IllegalArgumentException("Stop object should not be empty");
        }
        return stopRepository.save(point);
    }

    @Override
    @Transactional(readOnly = true)
    public Stop getById(Integer id) {
        return stopRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Stop with id '%s' not found", id)));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        try {
            stopRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Stop with id '%s' not found", id));
        }
    }

    @Override
    @Transactional
    public Stop update(Stop stop) {
        if (stop == null) {
            throw new IllegalArgumentException("Stop value should not be null!");
        }
        if (stopRepository.existsById(stop.getId())) {
            return stopRepository.save(stop);
        } else {
            throw new ResourceNotFoundException(String.format("Stop with id '%s' not found", stop.getId()));
        }
    }

    @Transactional
    public List<Stop> getByTransitId(Integer id) {
        return stopRepository.findByTransitId(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Stop getByTransitIdAndStopNameAndDirection(Integer transitId, String street, Stop.Direction direction) {
        return stopRepository.findByTransitIdAndStopNameAndDirection(transitId, street, direction);
    }

    @Override
    public List<Stop> getByTransitIdAndDirection(Integer id, Stop.Direction direction) {
        if (direction.equals(Stop.Direction.FORWARD)) {
            return stopRepository.findForwardStopsByTransitId(id);
        }
        return stopRepository.findBackwardStopsByTransitId(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer getIndexByTransitIdAndStopNameAndDirection(Integer transitId, String street,
                                                              Stop.Direction direction) {

        return stopRepository.findIndexByTransitIdAndStopNameAndDirection(transitId, street, direction);

    }

    @Override
    public List<Stop> getAll(Specification specification) {
        return stopRepository.findAll(specification);
    }
}
