package com.gfutac.restfilter.filter;

import lombok.Data;

@Data
public class FilterToken {
    private FilterTokenType tokenType;
    private Object value;

    public FilterToken(FilterTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public FilterToken(FilterTokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public SearchOperation getSearchOperation() {
        if (this.tokenType == FilterTokenType.SEARCH_OPERATION && this.value instanceof SearchOperation) {
            return (SearchOperation)value;
        }

        return null;
    }
}
