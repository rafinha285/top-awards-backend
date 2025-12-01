package org.animefoda.topawardsbackend.exception;

public class Unauthorized extends BaseError {
    public Unauthorized(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
