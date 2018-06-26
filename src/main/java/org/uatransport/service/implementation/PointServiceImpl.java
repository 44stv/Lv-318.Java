package org.uatransport.service.implementation;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Point;
import org.uatransport.entity.Stop;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.PointRepository;
import org.uatransport.repository.StopRepository;
import org.uatransport.service.PointService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final StopRepository stopRepository;

    @Override
    @Transactional
    public Point save(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Stop object should not be empty");
        }
        return pointRepository.save(point);
    }

    @Override
    @Transactional(readOnly = true)
    public Point getById(Integer id) {
        return pointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Stop with id '%s' not found", id)));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        try {
            pointRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Stop with id '%s' not found", id));
        }
    }

    @Override
    @Transactional
    public Point update(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Stop value should not be null!");
        }
        if (pointRepository.existsById(point.getId())) {
            return pointRepository.save(point);
        } else {
            throw new ResourceNotFoundException(String.format("Stop with id '%s' not found", point.getId()));
        }
    }

    @Override
    @Transactional
    public List<Stop> getByStreet(String street) {
        if (Strings.isNullOrEmpty(street)) {
            throw new IllegalArgumentException("Parameter street should not be null!");
        }
        return stopRepository.findByStreet(street);
    }

    @Transactional
    public List<Point> getByTransitId(Integer id) {
        return pointRepository.findByTransitId(id);
    }

    @Transactional
    public List<Stop> getStopsByTransitId(Integer id) {
        return stopRepository.findStopsByTransitId(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Stop getByTransitIdAndStopName(Integer transitId, String street) {
        return stopRepository.findByTransitIdAndStopName(transitId, street);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer getIndexByTransitIdAndStopName(Integer transitId, String street) {
        if (pointRepository.existsById(getByTransitIdAndStopName(transitId, street).getId())) {
            return stopRepository.findIndexByTransitIdAndStopName(transitId, street);
        } else {
            throw new ResourceNotFoundException("Stop  not found");
        }
    }
}
