package org.uatransport.repository;

import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.Transit;

import java.util.List;

public interface TransitRepository extends CrudRepository<Transit, Integer> {

    Transit findByName(String name);

    List<Transit> findByNameContaining(String name);

    List<Transit> findByCategoryName(String name);

    List<Transit> findByCategoryId(Integer id);

    List<Transit> findByCategoryNextLevelCategoryId(Integer id);

    List<Transit> findByCategoryNextLevelCategoryName(String name);

    // List<Transit> findByStopsIn(@Param("stops") Stop... stops);
}
