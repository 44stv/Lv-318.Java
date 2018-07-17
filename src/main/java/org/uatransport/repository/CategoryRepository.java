package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.ExtendableCategory;

import java.util.List;

public interface CategoryRepository
    extends CrudRepository<ExtendableCategory, Integer>, JpaSpecificationExecutor<ExtendableCategory> {

    List<ExtendableCategory> findAllByNextLevelCategoryIsNull();

}
