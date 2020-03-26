package com.gfutac.query.filter.spel;

import com.gfutac.query.filter.parser.FilterExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.*;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;

@Configuration
public class SpelContextConfiguration {

    @Bean
    public StandardEvaluationContext filterExpressionContext() {
        var evaluationContext = new StandardEvaluationContext();

        evaluationContext.addPropertyAccessor(new PropertyAccessor() {
            @Override
            public void write(EvaluationContext context, Object target, String name, Object newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public TypedValue read(EvaluationContext context, Object target, String name) {
                var specification = (Specification) target;
                return new TypedValue(specification);
            }

            @Override
            public Class<?>[] getSpecificTargetClasses() {
                return null;
            }

            @Override
            public boolean canWrite(EvaluationContext context, Object target, String name) {
                return false;
            }

            @Override
            public boolean canRead(EvaluationContext context, Object target, String name) {
                return target.getClass() == FilterExpression.class;
            }
        });

        evaluationContext.setOperatorOverloader(new OperatorOverloader() {

            private boolean isSpecification(Object operand) {
                if (operand instanceof Specification) return true;

                var interfaces = operand.getClass().getGenericInterfaces();
                return interfaces.length > 0 && Arrays.stream(interfaces).anyMatch(i -> i == Specification.class);
            }

            @Override
            public boolean overridesOperation(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
                switch (operation) {
                    case MULTIPLY:
                    case ADD:
                        return this.isSpecification(leftOperand) && this.isSpecification(rightOperand);
                    default:
                        return false;
                }
            }

            @Override
            public Object operate(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
                Specification result = null;

                var x = (Specification) leftOperand;
                var y = (Specification) rightOperand;

                switch (operation) {
                    case ADD:
                        result = x.or(y);
                        break;
                    case MULTIPLY:
                        result = x.and(y);
                        break;
                    default:
                        throw new EvaluationException("not supported operator: " + operation);
                }

                return result;
            }
        });

        return evaluationContext;
    }
}
