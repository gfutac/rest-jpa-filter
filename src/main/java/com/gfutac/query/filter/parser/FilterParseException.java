package com.gfutac.query.filter.parser;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class FilterParseException extends ParseCancellationException {
    public FilterParseException(String message) {
        super(message);
    }
}
