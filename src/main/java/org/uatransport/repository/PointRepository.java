package org.uatransport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Point;

import java.util.List;

public interface PointRepository extends GenericPointRepository<Point> {

    @Query("SELECT s FROM Transit t JOIN t.points s WHERE t.id = :id ORDER BY INDEX(s)")
    List<Point> findByTransitId(@Param("id") Integer id);

    boolean existsByLatAndLngAndDirection(Double lat, Double lng, Point.DIRECTION direction);

    Point getByLatAndLngAndDirection(Double lat, Double lng, Point.DIRECTION direction);
}
