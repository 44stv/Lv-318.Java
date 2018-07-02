package org.uatransport.service.implementation;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Transit;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.CategoryRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.TransitService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitServiceImpl implements TransitService {

    private final TransitRepository transitRepository;
    private final CategoryRepository nonExtendableCategoryRepository;

    @Override
    public boolean existsInCategory(String name, NonExtendableCategory category) {
        return transitRepository.findByCategoryName(name)
            .stream()
            .map(Transit::getCategory)
            .anyMatch(category::equals);
    }

    @Override
    @Transactional
    public Transit add(Transit transit) {
        if (transit == null) {
            throw new IllegalArgumentException("Transit object should not be null");
        }

        Integer categoryId = transit.getCategory().getId();

        if (nonExtendableCategoryRepository.existsById(categoryId)) {
            return transitRepository.save(transit);
        } else {
            throw new ResourceNotFoundException(String.format("Category with id '%s' not found", categoryId));
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        try {
            transitRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Transit with id '%s' not found", id));
        }
    }

    @Override
    @Transactional
    public void delete(Transit transit) {
        if (transit == null) {
            throw new IllegalArgumentException("Transit object should not be null");
        }
        try {
            transitRepository.delete(transit);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Transit with id '%s' not found", transit.getId()));
        }
    }

    @Override
    @Transactional
    public Transit update(Transit transit) {
        if (transit == null) {
            throw new IllegalArgumentException("Transit object should not be null");
        }
        if (transitRepository.existsById(transit.getId())) {
            return transitRepository.save(transit);
        } else {
            throw new ResourceNotFoundException(String.format("Transit with id '%s' not found", transit.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Transit getById(Integer id) {
        return transitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transit with id '%s' not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Transit getByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name should not be empty");
        }
        return transitRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transit> getAllByCategoryId(Integer id) {
        return transitRepository.findByCategoryId(id);
    }

    @Override
    public List<Transit> getAllByNextLevelCategoryId(Integer id) {
        return transitRepository.findByCategoryNextLevelCategoryId(id);
    }

    @Override
    public List<Transit> getAllByNextLevelCategoryName(String categoryName) {
        if (Strings.isNullOrEmpty(categoryName)) {
            throw new IllegalArgumentException("Category name should not be empty");
        }
        return transitRepository.findByCategoryNextLevelCategoryName(categoryName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transit> getAll() {
        return Streams.stream(transitRepository.findAll()).collect(Collectors.toList());
    }

    // @Override
    // @Transactional(readOnly = true)
    // public List<Transit> getTransitsByStopsIn(Stop[] stops) {
    // return transitRepository.findByStopsIn(stops);
    // }

    @Override
    public List<Transit> getAll(Specification specification) {
        return transitRepository.findAll(specification);
    }
}
