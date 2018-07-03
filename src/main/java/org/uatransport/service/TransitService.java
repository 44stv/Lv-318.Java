package org.uatransport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Transit;

import java.util.List;

public interface TransitService {

    boolean existsInCategory(String name, NonExtendableCategory category);

    Transit add(Transit transit);

    void delete(Integer id);

    void delete(Transit transit);

    Transit update(Transit transit);

    Transit getById(Integer id);

    Transit getByName(String name);

    Transit getByNameAndCategoryName(String name, String categoryName);

    List<Transit> getAllByCategoryId(Integer id);
    Page<Transit> getAllByCategoryIdByPage(Integer id, Pageable pageable);

    List<Transit> getAllByNextLevelCategoryId(Integer id);

    Page<Transit> getAllByNextLevelCategoryNameByPage(String categoryName, Pageable pageable);

    List<Transit> getAll();

    List<Transit> getAll(Specification specification);

}
