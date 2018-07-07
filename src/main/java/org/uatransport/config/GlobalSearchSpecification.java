package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class GlobalSearchSpecification implements Specification<Transit> {

    private final GlobalSearch globalSearch;
    public final List<Predicate> predicates = new ArrayList<>();


    private void filterBySearchName(Root<Transit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!globalSearch.getGlobalSearch().isEmpty()) {
            Predicate transitName = cb.like(root.get("name"), "%" + globalSearch.getGlobalSearch() + "%");
            Join<Transit, Stop> transitStopJoin = root.join("stops");
            Predicate stops = cb.like(transitStopJoin.get("street"), "%" + globalSearch.getGlobalSearch() + "%");
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
