package org.animefoda.topawardsbackend.exception

enum class ErrorCode(val httpStatusCode: Int) {
    NOT_FOUND(404),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    INTERNAL_SERVER_ERROR(500),
    VALIDATION_ERROR(400),
    INVALID_CAPTCHA(422),
    INVALID_TOKEN(401),
    EXISTS(409)
}