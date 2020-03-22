package com.gfutac.restfilter.filter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.LinkedList;

public class FilterParseTreeVisitor extends FilterBaseVisitor<Object> {

    private Deque<FilterToken> tokens;

    public FilterParseTreeVisitor() {
        this.tokens = new LinkedList<>();
    }

    public Deque<FilterToken> getTokens() {
        return this.tokens;
    }

    @Override
    public Object visitBinaryOrExpression(FilterParser.BinaryOrExpressionContext ctx) {
        this.tokens.add(new FilterToken(FilterTokenType.BINARY_OR));
        return super.visitBinaryOrExpression(ctx);
    }

    @Override
    public Object visitBinaryAndExpression(FilterParser.BinaryAndExpressionContext ctx) {
        this.tokens.add(new FilterToken(FilterTokenType.BINARY_AND));
        return super.visitBinaryAndExpression(ctx);
    }

    @Override
    public Object visitComparatorExpression(FilterParser.ComparatorExpressionContext ctx) {
        FilterTokenType tokenType = FilterTokenType.COMPARATOR_EQ;

        if (ctx.op.EQ() != null) tokenType = FilterTokenType.COMPARATOR_EQ;
        if (ctx.op.NE() != null) tokenType = FilterTokenType.COMPARATOR_NE;
        if (ctx.op.LIKE() != null) tokenType = FilterTokenType.COMPARATOR_LIKE;
        if (ctx.op.NLIKE() != null) tokenType = FilterTokenType.COMPARATOR_NLIKE;
        if (ctx.op.GT() != null) tokenType = FilterTokenType.COMPARATOR_GT;
        if (ctx.op.GE() != null) tokenType = FilterTokenType.COMPARATOR_GE;
        if (ctx.op.LE() != null) tokenType = FilterTokenType.COMPARATOR_LE;
        if (ctx.op.LT() != null) tokenType = FilterTokenType.COMPARATOR_LT;

        Object value = null;
        String txt = ctx.right.getText();
        if (ctx.right.STRING() != null) {
            if (txt.startsWith("\"") && txt.endsWith("\"")) {
                value = txt.substring(1, txt.length() - 1);
            }
        } else if (ctx.right.DECIMAL() != null) {
            if (txt.contains(".")) {
                value = Double.parseDouble(txt);
            } else {
                value = Long.parseLong(txt);
            }
        } else if (ctx.right.DATE() != null) {
            if (txt.startsWith("date\"") && txt.endsWith("\"")) {
                var tmp = txt.replace("date\"", "");
                tmp = tmp.substring(0, tmp.length() - 1);
                value = ZonedDateTime.parse(tmp, DateTimeFormatter.ISO_DATE_TIME);
            }
        }

        var op = new FilterExpression(ctx.left.getText(), tokenType, value);
        this.tokens.add(new FilterToken(FilterTokenType.EXPRESSION, op));

        return super.visitComparatorExpression(ctx);
    }
}

