package com.gfutac.restfilter.filter;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class FilterExpressionErrorListener extends BaseErrorListener {
    public static final FilterExpressionErrorListener INSTANCE = new FilterExpressionErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws FilterExpressionParseException {
        throw new FilterExpressionParseException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}
