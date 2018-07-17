package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.NonExtendableCategory;
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
            Predicate transitPredicate = cb.like(root.get("name"), globalSearch.getGlobalSearch() + "%");
            predicates.add(transitPredicate);
            // Join<Transit, Stop> transitStopJoin = root.join("stops");
            // Predicate stopsPredicate = cb.like(transitStopJoin.get("street"),
            // "%" + globalSearch.getGlobalSearch() + "%");
            // predicates.add(stopsPredicate);
            Join<Transit, NonExtendableCategory> nonExCategoryJoin = root.join("category");
            Predicate categoryPredicate = cb.like(nonExCategoryJoin.get("nextLevelCategory").get("name"),
                    "%" + globalSearch.getCity() + "%");
            predicates.add(categoryPredicate);
        }
    }

    @Override
    public Predicate toPredicate(Root<Transit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        criteriaQuery.distinct(true);
        filterBySearchName(root, criteriaQuery, criteriaBuilder);
        Predicate[] arrayOfPredicates = new Predicate[predicates.size()];
        predicates.toArray(arrayOfPredicates);
        return criteriaBuilder.and(arrayOfPredicates);
    }
}