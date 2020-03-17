package com.gfutac.restfilter.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SearchCriteria {
    public static final String GT = ">";
    public static final String LT = "<";
    public static final String EQ = ":";

    private String key;
    private String operation;
    private Object value;
}
