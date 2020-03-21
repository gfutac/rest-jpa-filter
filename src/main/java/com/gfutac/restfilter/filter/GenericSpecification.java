package com.gfutac.restfilter.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {
    protected SearchOperation criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path path = null;
        var nestedObjects = criteria.getOperand().split("\\.");
        if (nestedObjects.length == 0) nestedObjects = new String[] { criteria.getOperand() };
        for (var r : nestedObjects) {
            if (path == null) path = root.get(r);
            else path = path.get(r);
        }

        if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_EQ.getValue()) && criteria.getValue() == null) {
            return builder.isNull(path);
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_NE.getValue()) && criteria.getValue() == null) {
            return builder.isNotNull(path);
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_GT.getValue())) {
            return greaterThan(builder, path, criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_LT.getValue())) {
            return lessThan(builder, path, criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_EQ.getValue())) {
            if (path.getJavaType() == String.class) {
//                return builder.like(path, "%" + criteria.getValue() + "%");
                return equal(builder, path, criteria.getValue());
            } else {
                return equal(builder, path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_NE.getValue())) {
            if (path.getJavaType() == String.class) {
//                return builder.like(path, "%" + criteria.getValue() + "%");
                return notEqual(builder, path, criteria.getValue());
            } else {
                return notEqual(builder, path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase(FilterTokenType.COMPARATOR_LIKE.getValue())) {
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return equal(builder, path, criteria.getValue());
            }
        }

        return null;
    }

    private static ComparisonPredicate greaterThan(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.GREATER_THAN, path, value);
    }

    private static ComparisonPredicate greaterThanOrEqual(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.GREATER_THAN_OR_EQUAL, path, value);
    }

    private static ComparisonPredicate lessThan(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.LESS_THAN, path, value);
    }

    private static ComparisonPredicate lessThanOrEqual(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.LESS_THAN_OR_EQUAL, path, value);
    }

    private static ComparisonPredicate equal(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.EQUAL, path, value);
    }

    private static ComparisonPredicate notEqual(CriteriaBuilder builder, Path path, Object value) {
        return new ComparisonPredicate((CriteriaBuilderImpl) builder, ComparisonPredicate.ComparisonOperator.NOT_EQUAL, path, value);
    }

}
