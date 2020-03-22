package com.gfutac.restfilter.filter;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

@Slf4j
public class FilterExpressionErrorListener extends BaseErrorListener {
    public static final FilterExpressionErrorListener INSTANCE = new FilterExpressionErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws FilterExpressionParseException {

        var errorMessage = "line " + line + ":" + charPositionInLine + " " + msg;
        log.error(errorMessage);
        throw new FilterExpressionParseException(errorMessage);
    }
}
