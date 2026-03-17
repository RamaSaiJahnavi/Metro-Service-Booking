package com.moveinsync.metro.exception;

public class CustomException extends RuntimeException {

    private final String errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}