package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopRepository extends CrudRepository<Stop, Integer>, JpaSpecificationExecutor<Stop> {

    Stop findByLatAndLngAndDirection(Double lat, Double lng, Stop.Direction direction);

    @Query("SELECT s FROM Transit t JOIN t.stops s " + "WHERE t.id = :id ORDER BY INDEX(s)")
    List<Stop> findByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s "
        + "WHERE t.id = :id AND s.street = :street AND s.direction = :direction")
    Stop findByTransitIdAndStopNameAndDirection(@Param("id") Integer transitId, @Param("street") String street,
                                                @Param("direction") Stop.Direction direction);

    @Query("SELECT INDEX(s) FROM Transit t JOIN t.stops s "
        + "WHERE t.id = :id AND s.street = :street AND s.direction = :direction")
    Integer findIndexByTransitIdAndStopNameAndDirection(@Param("id") Integer transitId, @Param("street") String street,
                                                        @Param("direction") Stop.Direction direction);

    @Query("SELECT s FROM Transit t JOIN t.stops s WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='FORWARD' ORDER BY INDEX(s)")
    List<Stop> findForwardStopsByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s WHERE t.id = :id AND s.street IS NOT NULL AND s.direction ='BACKWARD' ORDER BY INDEX(s)")
    List<Stop> findBackwardStopsByTransitId(@Param("id") Integer id);

    @Query("SELECT s FROM Transit t JOIN t.stops s WHERE t.id = :id AND s.street IS NOT NULL AND s.direction = :direction ORDER BY INDEX(s)")
    List<Stop> findStopsByTransitIdAndDirection(@Param("id") Integer id, @Param("direction") Stop.Direction direction);

}
