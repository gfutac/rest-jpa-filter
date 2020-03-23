package com.gfutac.restfilter.filter.specification;

import com.gfutac.restfilter.filter.parser.FilterExpression;
import com.gfutac.restfilter.filter.parser.FilterTokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class GenericSpecification<T> implements Specification<T> {

    protected FilterExpression filterExpression;

    private static Map<FilterTokenType, Function<PredicateParameter, Predicate>> predicates = new HashMap<>() {
        {{
            put(FilterTokenType.COMPARATOR_EQ, GenericSpecification::equal);
            put(FilterTokenType.COMPARATOR_NE, GenericSpecification::notEqual);
            put(FilterTokenType.COMPARATOR_GT, GenericSpecification::greaterThan);
            put(FilterTokenType.COMPARATOR_GE, GenericSpecification::greaterThanOrEqual);
            put(FilterTokenType.COMPARATOR_LT, GenericSpecification::lessThan);
            put(FilterTokenType.COMPARATOR_LE, GenericSpecification::lessThanOrEqual);
            put(FilterTokenType.COMPARATOR_LIKE, GenericSpecification::like);
            put(FilterTokenType.COMPARATOR_NLIKE, GenericSpecification::notLike);
        }}
    };

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var attributePath = getPathToAttribute(root);
        var criteriaComparator = filterExpression.getComparator();

        var predicateParameter = new PredicateParameter(builder, attributePath, filterExpression.getValue());
        var predicate = GenericSpecification.predicates.get(criteriaComparator);

        if (predicate != null) {
            return predicate.apply(predicateParameter);
        }

        log.warn("Can not find appropriate predicate for {}.", criteriaComparator);
        return null;
    }

    private Path getPathToAttribute(Root<T> root) {
        Path path = null;
        var nestedObjects = filterExpression.getOperand().split("\\.");
        for (var r : nestedObjects) {
            if (path == null) path = root.get(r);
            else path = path.get(r);
        }

        return path;
    }

    private static ComparisonPredicate greaterThan(PredicateParameter param) {
        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.GREATER_THAN, param.getPath(), param.getValue());
    }

    private static ComparisonPredicate greaterThanOrEqual(PredicateParameter param) {
        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.GREATER_THAN_OR_EQUAL, param.getPath(), param.getValue());
    }

    private static ComparisonPredicate lessThan(PredicateParameter param) {
        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.LESS_THAN, param.getPath(), param.getValue());
    }

    private static ComparisonPredicate lessThanOrEqual(PredicateParameter param) {
        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.LESS_THAN_OR_EQUAL, param.getPath(), param.getValue());
    }

    private static Predicate equal(PredicateParameter param) {
        if (param.getValue() == null) {
            return param.getBuilder().isNull(param.getPath());
        }

        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.EQUAL, param.getPath(), param.getValue());
    }

    private static Predicate notEqual(PredicateParameter param) {
        if (param.getValue() == null) {
            return param.getBuilder().isNotNull(param.getPath());
        }

        return new ComparisonPredicate((CriteriaBuilderImpl) param.getBuilder(), ComparisonPredicate.ComparisonOperator.NOT_EQUAL, param.getPath(), param.getValue());
    }

    private static Predicate like(PredicateParameter param) {
        if (param.getPath().getJavaType() == String.class) {
            return param.getBuilder().like(param.getPath(), "%" + param.getValue() + "%");
        } else {
            return equal(param);
        }
    }

    private static Predicate notLike(PredicateParameter param) {
        if (param.getPath().getJavaType() == String.class) {
            return param.getBuilder().notLike(param.getPath(), "%" + param.getValue() + "%");
        } else {
            return notEqual(param);
        }
    }

    @Data
    @AllArgsConstructor
    private static class PredicateParameter {
        private CriteriaBuilder builder;
        private Path path;
        private Object value;
    }
}
