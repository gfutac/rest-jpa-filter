package com.gfutac.query.filter.parser;

import lombok.Getter;

@Getter
public class FilterToken {
    private FilterTokenType tokenType;
    private FilterExpression filterExpression;

    public FilterToken(FilterTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public FilterToken(FilterTokenType tokenType, FilterExpression filterExpression) {
        this.tokenType = tokenType;
        this.filterExpression = filterExpression;
    }
}
