package org.uatransport.service;

import org.uatransport.config.SearchCategoryParam;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.entity.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    ExtendableCategory save(ExtendableCategory category);

    void delete(Integer id);

    ExtendableCategory update(ExtendableCategory category);

    ExtendableCategory getById(Integer id);

    List<ExtendableCategory> getListTopExtendableCategories();

    void delete(ExtendableCategory extendableCategory);

    List<ExtendableCategory> getAll(SearchCategoryParam searchCategoryParam);

    List<CategoryDTO> getAllWithCountOfTransits(SearchCategoryParam searchCategoryParam);

}
