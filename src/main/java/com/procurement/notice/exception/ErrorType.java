package com.procurement.notice.exception;

public enum ErrorType {

    DATA_NOT_FOUND("00.01", "DATA not found."),
    IMPLEMENTATION_ERROR("00.01", "No implementation for this type of operation."),
    PARAM_ERROR("00.02", "Should not be empty for this type of operation");

    private final String code;
    private final String message;

    ErrorType(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
