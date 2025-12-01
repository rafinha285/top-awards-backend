package org.animefoda.topawardsbackend.response

import lombok.Builder
import lombok.Getter
import lombok.Setter
import org.animefoda.topawardsbackend.exception.ErrorCode
import java.io.Serializable
import java.time.Instant

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String? = null,
    val timestamp: Instant = Instant.now(),
    val errorCode: ErrorCode? = null,
) : Serializable{

    constructor(data: T) : this(true, data, null, Instant.now(), null )
    constructor(data: T, message: String?) : this(true, data, message, Instant.now(), null)
    constructor(success: Boolean, data: T): this(success,data, null, Instant.now(), null )

    constructor(success: Boolean, message: String?): this(success, null, message, Instant.now(), null )
    constructor(message: String, errorCode: ErrorCode): this(false, null, message, Instant.now(), errorCode)

    companion object {
        @JvmStatic
        fun <T> success(data: T?): ApiResponse<T> {
            return ApiResponse<T>(success = true, data = data)
        }

        @JvmStatic
        fun <T> success(data: T?, message: String?): ApiResponse<T> {
            return ApiResponse<T>(success = true, data = data, message = message)
        }

        @JvmStatic
        fun <T> error(message: String, errorCode: ErrorCode): ApiResponse<T> {
            return ApiResponse(
                success = false,
                data = null,
                message = message,
                errorCode = errorCode
            )
        }
    }
}