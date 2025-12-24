package com.rootydev.ProductAnalytics.specifications;

import com.rootydev.ProductAnalytics.dtos.SearchTermFilter;
import com.rootydev.ProductAnalytics.models.SearchTerm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchTermSpecification {

    public static Specification<SearchTerm> withFilter(SearchTermFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by query (case-insensitive partial match)
            if (filter.getQuery() != null && !filter.getQuery().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("query")),
                        "%" + filter.getQuery().toLowerCase() + "%"
                ));
            }

            // Filter by date range
            if (filter.getStartDate() != null && !filter.getStartDate().isEmpty()) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getStartDate()
                ));
            }

            if (filter.getEndDate() != null && !filter.getEndDate().isEmpty()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getEndDate()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}