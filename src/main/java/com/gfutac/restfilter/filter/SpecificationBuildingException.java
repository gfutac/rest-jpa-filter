package com.gfutac.restfilter.filter;

public class SpecificationBuildingException extends Exception {
    public SpecificationBuildingException() {
        super();
    }

    public SpecificationBuildingException(String message) {
        super(message);
    }

    public SpecificationBuildingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecificationBuildingException(Throwable cause) {
        super(cause);
    }

    protected SpecificationBuildingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
