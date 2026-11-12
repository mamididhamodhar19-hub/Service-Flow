package com.serviceflow.exception;

/**
 * Thrown when a business rule is violated (e.g. illegal status transition,
 * duplicate email registration, SLA rule mismatch).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
