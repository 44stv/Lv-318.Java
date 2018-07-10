package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class StopSearchSpecification implements Specification<Stop> {
    private final GlobalSearch globalSearch;

    @Override
    public Predicate toPredicate(Root<Stop> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (globalSearch.getGlobalSearch().isEmpty())
            return null;
        return criteriaBuilder.like(criteriaBuilder.lower(root.get("street")),
                "%" + globalSearch.getGlobalSearch().toLowerCase() + "%");
    }
}
