package com.procurement.notice.exception;

public enum ErrorType {

    DATA_NOT_FOUND("00.01", "Data not found."),
    IMPLEMENTATION_ERROR("00.02", "No implementation for this type of operation."),
    PARAM_ERROR("00.03", "Should not be empty for this type of operation"),
    MS_NOT_FOUND("00.04", "MS not found."),
    RECORD_NOT_FOUND("00.05", "Record not found."),
    AWARD_NOT_FOUND("00.06", "Award not found."),
    BID_NOT_FOUND("00.07", "Bid not found."),
    LOT_NOT_FOUND("00.08", "Lot not found.");

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
