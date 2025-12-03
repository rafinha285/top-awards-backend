package org.animefoda.topawardsbackend.exception;

public class ValidationException extends BaseError {
    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }
}
