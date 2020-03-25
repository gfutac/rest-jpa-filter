package com.gfutac.query.filter.specification;

import com.gfutac.query.filter.FilterLexer;
import com.gfutac.query.filter.FilterParser;
import com.gfutac.query.filter.parser.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FilterSpecificationBuilder<T> {

    private Specification<T> builtSpecification;

    public FilterSpecificationBuilder<T> and(String key, FilterTokenType operation, Object value) {
        var filterExpression = new FilterExpression(key, operation, value);
        var specification = new FilterSpecification<T>(filterExpression);

        if (this.builtSpecification == null) {
            this.builtSpecification = specification;
        } else {
            this.builtSpecification = this.builtSpecification.and(specification);
        }

        return this;
    }

    public FilterSpecificationBuilder<T> or(String key, FilterTokenType operation, Object value) {
        var filterExpression = new FilterExpression(key, operation, value);
        var specification = new FilterSpecification<T>(filterExpression);

        if (this.builtSpecification == null) {
            this.builtSpecification = specification;
        } else {
            this.builtSpecification = this.builtSpecification.or(specification);
        }

        return this;
    }

    public Specification<T> build() {
        return this.builtSpecification;
    }

    public Specification<T> build(String expression) throws SpecificationBuildingException {
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);
        var visitor = new FilterParseTreeVisitor();

        try {
            visitor.visitParse(parser.parse());
        } catch (FilterParseException ex) {
            log.error("Could not parse expression. Specification will not be built.", ex);
            throw new SpecificationBuildingException(ex);
        }

        var prefixExpression = visitor.getTokens();
        Collections.reverse((List<?>) prefixExpression);

        var stack = new Stack<Specification<T>>();

        for (var token : prefixExpression) {
            if (token.getTokenType() == FilterTokenType.EXPRESSION) {
                stack.push(new FilterSpecification<>(token.getFilterExpression()));
            } else {
                var firstOperand = stack.pop();
                var secondOperand = stack.pop();

                if (token.getTokenType() == FilterTokenType.BINARY_OR) {
                    stack.push(firstOperand.or(secondOperand));
                } else if (token.getTokenType() == FilterTokenType.BINARY_AND) {
                    stack.push(firstOperand.and(secondOperand));
                }
            }
        }

        return stack.pop();
    }
}
