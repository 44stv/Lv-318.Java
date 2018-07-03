package org.uatransport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopRepository extends CrudRepository<Stop, Integer> {

    boolean existsByLatAndLngAndDirection(Double lat, Double lng, Stop.DIRECTION direction);

    Stop getByLatAndLngAndDirection(Double lat, Double lng, Stop.DIRECTION direction);

    @Query("SELECT s FROM Transit t JOIN t.stops s "
            + "WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='FORWARD'ORDER BY INDEX(s)")
    List<Stop> findByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s "
            + "WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='FORWARD' AND s.street = :street")
    Stop findByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);

    @Query("SELECT INDEX(s) FROM Transit t JOIN t.stops s "
            + "WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='FORWARD' AND s.street = :street")
    Integer findIndexByTransitIdAndStopName(@Param("id") Integer transitId, @Param("street") String street);

    @Query("SELECT s FROM Transit t JOIN t.stops s "
            + "WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='FORWARD' ORDER BY INDEX(s)")
    List<Stop> findForwardStopsByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s "
            + "WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='BACKWARD' ORDER BY INDEX(s)")
    List<Stop> findBackwardStopsByTransitId(@Param("id") Integer id);

}
