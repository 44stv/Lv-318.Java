package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.Transit;

import java.util.List;

public interface TransitRepository extends CrudRepository<Transit, Integer>, JpaSpecificationExecutor<Transit> {

    Transit findByName(String name);

    Transit findByNameAndCategoryName(String name, String categoryName);

    List<Transit> findByNameContaining(String name);

    List<Transit> findByCategoryName(String name);

    List<Transit> findByCategoryId(Integer id);

    List<Transit> findByCategoryNextLevelCategoryId(Integer id);

    List<Transit> findByCategoryNextLevelCategoryName(String name);

}
