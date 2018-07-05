package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public Stop getByTransitIdAndStopNameAndDirection(Integer transitId, String street, String direction) {
        return stopRepository.findByTransitIdAndStopNameAndDirection(transitId, street, direction);
    }

    @Override
    public List<Stop> getByTransitIdAndDirection(Integer id, String direction) {
        if (direction.equals("forward")) {
            return stopRepository.findForwardStopsByTransitId(id);
        }
        return stopRepository.findBackwardStopsByTransitId(id);
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public Integer getIndexByTransitIdAndStopName(Integer transitId, String street) {
//        if (stopRepository.existsById(getByTransitIdAndStopNameAndDirection(transitId, street).getId())) {
//            return stopRepository.findIndexByTransitIdAndStopName(transitId, street);
//        } else {
//            throw new ResourceNotFoundException("Stop  not found");
//        }
//    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer getIndexByTransitIdAndStopNameAndDirection(Integer transitId, String street, String direction) {
        if (stopRepository.existsById(getByTransitIdAndStopNameAndDirection(transitId, street, direction).getId())) {
            return stopRepository.findIndexByTransitIdAndStopNameAndDirection(transitId, street, direction);
        } else {
            throw new ResourceNotFoundException("Stop  not found");
        }
    }
}
