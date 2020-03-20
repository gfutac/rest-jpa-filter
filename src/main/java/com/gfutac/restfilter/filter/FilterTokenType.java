package com.gfutac.restfilter.filter;

public enum FilterTokenType {
    COMPARATOR_GT(">"),
    COMPARATOR_GE(">="),
    COMPARATOR_LT("<"),
    COMPARATOR_LE("<="),
    COMPARATOR_EQ("="),
    COMPARATOR_LIKE("~"),

    BINARY_AND("AND"),
    BINARY_OR("OR"),

    LPAREN("("),
    RPAREN(")"),

    SEARCH_OPERATION("search_operation");

    private String value;

    FilterTokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
