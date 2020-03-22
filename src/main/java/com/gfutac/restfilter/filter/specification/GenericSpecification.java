package com.gfutac.restfilter.filter.specification;

import com.gfutac.restfilter.filter.parser.FilterExpression;
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
    protected FilterExpression criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path path = null;
        var nestedObjects = criteria.getOperand().split("\\.");
        if (nestedObjects.length == 0) nestedObjects = new String[] { criteria.getOperand() };
        for (var r : nestedObjects) {
            if (path == null) path = root.get(r);
            else path = path.get(r);
        }

        var op = criteria.getComparison();
        switch(op) {
            case COMPARATOR_EQ:
                if (criteria.getValue() == null) return builder.isNull(path);
                return equal(builder, path, criteria.getValue());
            case COMPARATOR_NE:
                if (criteria.getValue() == null) return builder.isNotNull(path);
                return notEqual(builder, path, criteria.getValue());
            case COMPARATOR_GT:
                return greaterThan(builder, path, criteria.getValue());
            case COMPARATOR_GE:
                return greaterThanOrEqual(builder, path, criteria.getValue());
            case COMPARATOR_LT:
                return lessThan(builder, path, criteria.getValue());
            case COMPARATOR_LE:
                return lessThanOrEqual(builder, path, criteria.getValue());
            case COMPARATOR_LIKE:
                if (path.getJavaType() == String.class) {
                    return builder.like(path, "%" + criteria.getValue() + "%");
                } else {
                    return equal(builder, path, criteria.getValue());
                }
            case COMPARATOR_NLIKE:
                if (path.getJavaType() == String.class) {
                    return builder.notLike(path, "%" + criteria.getValue() + "%");
                } else {
                    return notEqual(builder, path, criteria.getValue());
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
