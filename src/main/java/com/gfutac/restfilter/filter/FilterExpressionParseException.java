package com.gfutac.restfilter.filter;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class FilterExpressionParseException extends ParseCancellationException {
    public FilterExpressionParseException(String message) {
        super(message);
    }
}
