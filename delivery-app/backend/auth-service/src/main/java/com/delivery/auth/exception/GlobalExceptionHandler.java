package com.delivery.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Error interno: " + ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse("Error: " + ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

