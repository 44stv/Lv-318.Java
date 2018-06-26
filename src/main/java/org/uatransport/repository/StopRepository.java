package org.uatransport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopRepository extends GenericPointRepository<Stop> {

    List<Stop> findByStreet(String name);

    @Query("SELECT s FROM Transit t JOIN t.points s WHERE t.id = :id AND s.street IS NOT NULL ORDER BY INDEX(s)")
    List<Stop> findStopsByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.points s WHERE t.id = :id AND s.street = :street")
    Stop findByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);

    @Query("SELECT INDEX(s) FROM Transit t JOIN t.points s WHERE t.id = :id AND s.street = :street")
    Integer findIndexByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);
}
