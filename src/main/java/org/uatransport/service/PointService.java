package org.uatransport.service;

import org.uatransport.entity.Point;

import java.util.List;

public interface PointService {
    Point save(Point stop);

    Point getById(Integer id);

    void delete(Integer id);

    Point update(Point stop);

    List<Point> getByStreet(String street);

    List<Point> getByTransitId(Integer id);

    Point getByTransitIdAndStopName(Integer transitId, String street);

    Integer getIndexByTransitIdAndStopName(Integer transitId, String street);
}
