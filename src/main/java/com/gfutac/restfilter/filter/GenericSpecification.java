package com.gfutac.restfilter.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {
    protected SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> path = null;
        var nestedObjects = criteria.getKey().split("\\.");
        if (nestedObjects.length == 0) nestedObjects = new String[] { criteria.getKey() };
        for (var r : nestedObjects) {
            if (path == null) path = root.get(r);
            else path = path.get(r);
        }

        if (criteria.getOperation().equalsIgnoreCase(SearchCriteria.GT)) {
            return builder.greaterThan(path, criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchCriteria.LT)) {
            return builder.lessThan(path, criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchCriteria.EQ)) {
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }

        return null;
    }
}
