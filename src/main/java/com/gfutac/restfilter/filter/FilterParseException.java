package com.gfutac.restfilter.filter;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class FilterParseException extends ParseCancellationException {
    public FilterParseException(String message) {
        super(message);
    }
}
