package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.config.SearchCategoryParam;
import org.uatransport.config.SearchSpecification;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.CategoryRepository;
import org.uatransport.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ExtendableCategory save(ExtendableCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(ExtendableCategory extendableCategory) {
        if (extendableCategory == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        try {
            categoryRepository.delete(extendableCategory);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(
                    String.format("Category with id '%s' not found", extendableCategory.getId()));
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Category with id '%s' not found", id));
        }
    }

    @Override
    @Transactional
    public ExtendableCategory update(ExtendableCategory categoryToUpdate) {
        if (categoryRepository.existsById(categoryToUpdate.getId())) {
            return categoryRepository.save(categoryToUpdate);
        } else {
            throw new ResourceNotFoundException(
                    String.format("Category with id '%s' not found", categoryToUpdate.getId()));
        }
    }

    @Override
    public ExtendableCategory getById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id '%s' not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtendableCategory> getListTopExtendableCategories() {
        return categoryRepository.findAllByNextLevelCategoryIsNull();
    }

    public List<ExtendableCategory> getAll(SearchCategoryParam searchCategoryParam) {
        if (searchCategoryParam.isEmpty()) {
            return new ArrayList<>();
        }

        SearchSpecification specification = new SearchSpecification(searchCategoryParam);

        return categoryRepository.findAll(specification);
    }
}
