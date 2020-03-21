package com.gfutac.restfilter.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Slf4j
public class GenericSpecificationBuilder<T> {

    private List<FilterExpression> criteria;

    public GenericSpecificationBuilder() {
        this.criteria = new ArrayList<>();
    }

    public GenericSpecificationBuilder<T> with(String key, FilterTokenType operation, Object value) {
        this.criteria.add(new FilterExpression(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (this.criteria.size() == 0) return null;

        var specifications = this.criteria.stream()
                .map((Function<FilterExpression, Specification<T>>) GenericSpecification::new)
                .collect(Collectors.toList());

        var result = specifications.get(0);
        result = specifications.stream()
                .skip(1)
                .reduce(result, (tmp, element) -> Specification.where(tmp).and(element));

        return result;
    }

    public Specification<T> build(String expression) {
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        var visitor = new FilterExpressionVisitor();;
        visitor.visitParse(parser.parse());

        var prefixExpression = visitor.getTokens();
        Collections.reverse((List<?>)prefixExpression);

        var stack = new Stack<Specification<T>>();

        for (var token : prefixExpression) {
            if (token.getTokenType() == FilterTokenType.EXPRESSION) {
                stack.push(new GenericSpecification<>(token.getFilterExpression()));
            } else {
                var firstOperand = stack.pop();
                var secondOperand = stack.pop();

                if (token.getTokenType() == FilterTokenType.BINARY_OR) {
                    stack.push(Specification.where(firstOperand).or(secondOperand));
                } else if (token.getTokenType() == FilterTokenType.BINARY_AND) {
                    stack.push(Specification.where(firstOperand).and(secondOperand));
                }
            }
        }

        return stack.pop();
    }
}
