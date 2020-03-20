package com.gfutac.restfilter.filter;

import java.util.Deque;
import java.util.LinkedList;

public class FilterVisitorImpl extends FilterBaseVisitor<Object> {

    private Deque<FilterToken> tokens;

    public FilterVisitorImpl() {
        this.tokens = new LinkedList<>();
    }

    public Deque<FilterToken> getTokens() {
        return this.tokens;
    }

    @Override
    public Object visitParse(FilterParser.ParseContext ctx) {
        return super.visitParse(ctx);
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
    public Object visitParenExpression(FilterParser.ParenExpressionContext ctx) {
        var res = super.visitParenExpression(ctx);
        return res;
    }

    @Override
    public Object visitComparatorExpression(FilterParser.ComparatorExpressionContext ctx) {
        FilterTokenType tokenType = FilterTokenType.COMPARATOR_EQ;
        if (ctx.op.EQ() != null) tokenType = FilterTokenType.COMPARATOR_EQ;
        if (ctx.op.LIKE() != null) tokenType = FilterTokenType.COMPARATOR_LIKE;
        if (ctx.op.GT() != null) tokenType = FilterTokenType.COMPARATOR_GT;
        if (ctx.op.GE() != null) tokenType = FilterTokenType.COMPARATOR_GE;
        if (ctx.op.LE() != null) tokenType = FilterTokenType.COMPARATOR_LE;
        if (ctx.op.LT() != null) tokenType = FilterTokenType.COMPARATOR_LT;

        var op = new SearchOperation(ctx.left.getText(), tokenType.getValue(), ctx.right.getText());
        this.tokens.add(new FilterToken(FilterTokenType.SEARCH_OPERATION, op));

        return super.visitComparatorExpression(ctx);
    }

    @Override
    public Object visitComparator(FilterParser.ComparatorContext ctx) {
        return super.visitComparator(ctx);
    }

    @Override
    public Object visitValue(FilterParser.ValueContext ctx) {
        return super.visitValue(ctx);
    }
}

