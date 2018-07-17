package org.uatransport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.uatransport.entity.ExtendableCategory;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SearchSpecification implements Specification<ExtendableCategory> {
    private final SearchCategoryParam searchCategoryParam;

    @Override
    public Predicate toPredicate(Root<ExtendableCategory> category, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.disjunction();

        List<Expression<Boolean>> expressions = new ArrayList<>();

        Join<Object, ExtendableCategory> firstNestedCategory = category.join("nextLevelCategory");

        if (searchCategoryParam.getFirstNestedCategoryName() != null) {
            expressions.add(
                    builder.equal(firstNestedCategory.get("name"), searchCategoryParam.getFirstNestedCategoryName()));
        }

        if (searchCategoryParam.getSecondNestedCategoryName() != null) {
            Join<Object, ExtendableCategory> secondNestedCategory = firstNestedCategory.join("nextLevelCategory");
            expressions.add(
                    builder.equal(secondNestedCategory.get("name"), searchCategoryParam.getSecondNestedCategoryName()));
        }

        if (searchCategoryParam.getName() != null) {
            expressions.add(builder.equal(category.get("name"), searchCategoryParam.getName()));
        }

        if (searchCategoryParam.getId() != null) {
            expressions.add(builder.equal(category.get("id"), searchCategoryParam.getId()));
        }

        predicate.getExpressions().add(builder.and(expressions.stream().toArray(Predicate[]::new)));

        return predicate;
    }
}
