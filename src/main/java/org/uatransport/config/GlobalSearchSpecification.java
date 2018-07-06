package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GlobalSearchSpecification implements Specification<Transit> {

    private final GlobalSearch globalSearch;
    public final List<Predicate> predicates = new ArrayList<>();

    private void filterBySearchName(Root<Transit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!globalSearch.getGlobalSearch().isEmpty()) {
            Predicate transitName = cb.like(root.get("name"), globalSearch.getGlobalSearch() + "%");
            Join<Transit, Stop> transitStopJoin = root.join("stops");
            Predicate stops = cb.like(transitStopJoin.get("street"), globalSearch.getGlobalSearch() + "%");
            predicates.add(cb.or(transitName, stops));
        }

    }

    @Override
    public Predicate toPredicate(Root<Transit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        criteriaQuery.distinct(true);
        filterBySearchName(root, criteriaQuery, criteriaBuilder);
        Predicate[] array = new Predicate[predicates.size()];
        predicates.toArray(array);
        return criteriaBuilder.and(array);
    }
}
