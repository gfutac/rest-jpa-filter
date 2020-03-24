package com.gfutac.query.filter.parser;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

@Slf4j
public class FilterParseErrorListener extends BaseErrorListener {
    public static final FilterParseErrorListener INSTANCE = new FilterParseErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws FilterParseException {

        var errorMessage = "line " + line + ":" + charPositionInLine + " " + msg;
        log.error(errorMessage);
        throw new FilterParseException(errorMessage);
    }
}
