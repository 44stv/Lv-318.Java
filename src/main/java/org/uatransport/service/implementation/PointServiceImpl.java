package org.uatransport.service.implementation;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Point;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.PointRepository;
import org.uatransport.service.PointService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

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

    @Transactional
    public List<Point> getByTransitId(Integer id) {
        return pointRepository.findByTransitId(id);
    }

}
