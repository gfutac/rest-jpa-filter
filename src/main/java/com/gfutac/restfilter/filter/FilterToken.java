package com.gfutac.restfilter.filter;

import lombok.Data;

@Data
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
