package org.uatransport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Transit;

import java.util.List;

public interface TransitService {

    Transit add(Transit transit);

    Transit getById(Integer id);

    Transit getByName(String name);

    Page<Transit> getAllByCategoryIdByPage(Integer id, Pageable pageable);

    List<Transit> getAllByNextLevelCategoryId(Integer id);

    Page<Transit> getAllByNextLevelCategoryNameByPage(String categoryName, Pageable pageable);

    List<Transit> getAll();

    List<Transit> getAll(Specification specification);

    Transit update(Transit transit);

    //    Transit upsert(Transit transit);

    void delete(Integer id);

    void delete(Transit transit);

}
