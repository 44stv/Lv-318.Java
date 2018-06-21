package org.uatransport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Point;

import java.util.List;

public interface PointRepository extends CrudRepository<Point, Integer> {

    List<Point> findByStreet(String name);

    @Query("SELECT s FROM Transit t JOIN t.stops s WHERE t.id = :id ORDER BY INDEX(s)")
    List<Point> findByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s WHERE t.id = :id AND s.street = :street")
    Point findByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);

    @Query("SELECT INDEX(s) FROM Transit t JOIN t.stops s WHERE t.id = :id AND s.street = :street")
    Integer findIndexByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);
}
