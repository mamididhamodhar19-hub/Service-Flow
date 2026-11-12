package com.serviceflow.exception;

/**
 * Thrown when the caller does not have permission to perform the requested operation.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
