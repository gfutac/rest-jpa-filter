package com.gfutac.query.filter.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterExpression {
    private String operand;
    private FilterTokenType comparator;
    private Object value;
}
