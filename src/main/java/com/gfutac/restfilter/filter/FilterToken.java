package com.gfutac.restfilter.filter;

import lombok.Data;

@Data
public class FilterToken {
    private FilterTokenType tokenType;
    private SearchOperation searchOperation;

    public FilterToken(FilterTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public FilterToken(FilterTokenType tokenType, SearchOperation searchOperation) {
        this.tokenType = tokenType;
        this.searchOperation = searchOperation;
    }
}
