package org.animefoda.topawardsbackend.exception;

import org.animefoda.topawardsbackend.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseError.class)
    public ResponseEntity<ApiResponse> handleBaseError(BaseError baseError) {
        ApiResponse<Object> response = ApiResponse.error(baseError.getMessage(), baseError.getErrorCode());
//        baseError.printStackTrace();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(baseError.getErrorCode().getHttpStatusCode()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        ApiResponse<Object> response = ApiResponse.error(badCredentialsException.getMessage(), badCredentialsException.getErrorCode());
        return new  ResponseEntity<>(response, HttpStatusCode.valueOf(badCredentialsException.getErrorCode().getHttpStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                ErrorCode.INTERNAL_SERVER_ERROR
        );
        ex.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage()) // Pega a mensagem definida na anotação
                .findFirst()
                .orElse("Erro de validação desconhecido");

        ApiResponse<Object> response = ApiResponse.error(errorMessage, ErrorCode.VALIDATION_ERROR);
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        // 1. Instancia sua classe customizada
        Unauthorized customError = new Unauthorized("Acesso negado: Você não possui permissão para realizar esta ação.");

        // 2. Reutiliza sua lógica padrão de BaseError para montar o JSON
        return handleBaseError(customError);
    }
}
