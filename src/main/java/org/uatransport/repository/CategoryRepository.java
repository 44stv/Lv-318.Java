package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.entity.NonExtendableCategory;

import java.util.List;

public interface CategoryRepository
        extends CrudRepository<ExtendableCategory, Integer>, JpaSpecificationExecutor<ExtendableCategory> {

    List<ExtendableCategory> findAllByNextLevelCategoryIsNull();
    List<ExtendableCategory> findByNextLevelCategory_Name(String name);

    ExtendableCategory findByNameAndNextLevelCategory_Name(String name, String nextLevelName);

}
