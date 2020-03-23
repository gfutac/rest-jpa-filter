package com.gfutac.restfilter.filter.parser;

import com.gfutac.restfilter.filter.FilterBaseVisitor;
import com.gfutac.restfilter.filter.FilterParser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Deque;
import java.util.LinkedList;

@Slf4j
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
        FilterTokenType tokenType = null;

        if (ctx.op.EQ() != null) tokenType = FilterTokenType.COMPARATOR_EQ;
        else if (ctx.op.NE() != null) tokenType = FilterTokenType.COMPARATOR_NE;
        else if (ctx.op.LIKE() != null) tokenType = FilterTokenType.COMPARATOR_LIKE;
        else if (ctx.op.NLIKE() != null) tokenType = FilterTokenType.COMPARATOR_NLIKE;
        else if (ctx.op.GT() != null) tokenType = FilterTokenType.COMPARATOR_GT;
        else if (ctx.op.GE() != null) tokenType = FilterTokenType.COMPARATOR_GE;
        else if (ctx.op.LE() != null) tokenType = FilterTokenType.COMPARATOR_LE;
        else if (ctx.op.LT() != null) tokenType = FilterTokenType.COMPARATOR_LT;

        Object value = null;
        String txt = ctx.right.getText();
        if (ctx.right.STRING() != null) {
            value = this.getStringValue(txt);
        } else if (ctx.right.NUMERIC() != null) {
            value = this.getNumericValue(txt);
        } else if (ctx.right.DATE() != null) {
            value = getDateValue(txt);
        }

        var filterExpression = new FilterExpression(ctx.left.getText(), tokenType, value);
        this.tokens.add(new FilterToken(FilterTokenType.EXPRESSION, filterExpression));

        return super.visitComparatorExpression(ctx);
    }

    private Number getNumericValue(@NonNull String str) {
        Number value
                ;
        if (str.contains(".")) value = this.getDoubleValue(str);
        else value = this.getLongValue(str);

        return value;
    }

    private Double getDoubleValue(@NonNull String str) {
        Double value = null;

        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException ne) {
            log.error("Can not parse {} as double.", str);
        }

        return value;
    }

    private Long getLongValue(@NonNull String str) {
        Long value = null;

        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException ne) {
            log.error("Can not parse {} as long.", str);
        }

        return value;
    }

    private String getStringValue(@NonNull String str) {
        String value = null;

        if (str.startsWith("\"") && str.endsWith("\"")) {
            value = str
                    .substring(1, str.length() - 1)
                    .trim()
                    .replace("\\\"", "\"");
        }

        return value;
    }

    private ZonedDateTime getDateValue(@NonNull String str) {
        ZonedDateTime value = null;

        if (str.startsWith("date\"") && str.endsWith("\"")) {
            var tmp = str.replace("date\"", "");
            tmp = tmp.substring(0, tmp.length() - 1);

            try {
                value = ZonedDateTime.parse(tmp, DateTimeFormatter.ISO_DATE_TIME);
            } catch(DateTimeParseException e) {
                log.error("Can not parse {} as ZonedDateTime.", str);
            }
        }

        return value;
    }
}

