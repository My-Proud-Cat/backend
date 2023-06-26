package com.study.proudcat.infra.exception.handler;

import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.ErrorResponse;
import com.study.proudcat.infra.exception.RestApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse> handleRestApiException(RestApiException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(errorCode.name())
                .statusCode(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .error(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
