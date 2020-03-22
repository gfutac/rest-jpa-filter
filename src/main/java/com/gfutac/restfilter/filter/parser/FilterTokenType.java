package com.gfutac.restfilter.filter.parser;

public enum FilterTokenType {
    COMPARATOR_GT(),
    COMPARATOR_GE(),
    COMPARATOR_LT(),
    COMPARATOR_LE(),
    COMPARATOR_EQ(),
    COMPARATOR_NE(),
    COMPARATOR_LIKE(),
    COMPARATOR_NLIKE(),

    BINARY_AND(),
    BINARY_OR(),

    EXPRESSION()
}
