package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StopSearchSpecification implements Specification<Stop> {
    private final GlobalSearch globalSearch;

    public final List<Predicate> predicates = new ArrayList<>();

    private void filterBySearchName(Root<Stop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!globalSearch.getGlobalSearch().isEmpty()) {
            Predicate stops = cb.like(cb.lower(root.get("street")),
                "%" + globalSearch.getGlobalSearch().toLowerCase() + "%");
            predicates.add(stops);
        }

    }

    @Override
    public Predicate toPredicate(Root<Stop> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (globalSearch.getGlobalSearch().isEmpty()) {
            return null;
        }
        query.distinct(true);
        filterBySearchName(root, query, criteriaBuilder);
        Predicate[] array = new Predicate[predicates.size()];
        predicates.toArray(array);
        return criteriaBuilder.and(array);
//        query.distinct(true);
//        return criteriaBuilder.like(criteriaBuilder.lower(root.get("street")),
//            "%" + globalSearch.getGlobalSearch().toLowerCase() + "%");
    }
}
