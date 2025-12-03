package org.animefoda.topawardsbackend.exception;

public class AlreadyExistsException extends BaseError {
    public AlreadyExistsException(String type) {
        super(type, ErrorCode.EXISTS);
    }
}
