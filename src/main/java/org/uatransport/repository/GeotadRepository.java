package org.uatransport.repository;

import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.Geotag;

public interface GeotadRepository extends CrudRepository<Geotag, Integer> {
}
