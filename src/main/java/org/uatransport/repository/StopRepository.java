package org.uatransport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uatransport.entity.Stop;

import java.util.List;

public interface StopRepository extends CrudRepository<Stop, Integer> {

    List<Stop> findByStreet(String name);
  @Query(value = "select * from stop join transit_stop on stop.id = transit_stop.stop_id " +
         "where transit_stop.transit_id = :id order by stop_index", nativeQuery = true)
    List<Stop> findByTransitId(@Param("id") Integer id);
}

