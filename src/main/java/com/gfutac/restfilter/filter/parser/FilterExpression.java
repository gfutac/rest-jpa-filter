package com.gfutac.restfilter.filter.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterExpression {
    private String operand;
    private FilterTokenType comparison;
    private Object value;
}
