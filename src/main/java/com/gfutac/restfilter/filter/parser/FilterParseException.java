package com.gfutac.restfilter.filter.parser;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class FilterParseException extends ParseCancellationException {
    public FilterParseException(String message) {
        super(message);
    }
}
