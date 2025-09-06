package com.esprit.adapters.input.rest.exception;


import com.esprit.adapters.input.rest.data.response.ErrorResponse;
import com.esprit.adapters.input.rest.exception.utils.ExceptionUtility;
import com.esprit.domain.model.exception.AppBusinessException;
import com.esprit.domain.model.exception.AppNotFoundException;
import com.esprit.domain.model.exception.AppTechnicalException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AppTechnicalException.class })
    public ResponseEntity<Object> handleAppTechnicalException(AppTechnicalException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getDescription(false))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { AppBusinessException.class })
    public ResponseEntity<Object> handleAppBusinessException(AppBusinessException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .errorKey(ex.getErrorKey())
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { AppNotFoundException.class })
    public ResponseEntity<Object> handleAppNotFoundException(AppNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getDescription(false))
                .status(HttpStatus.FORBIDDEN)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                                                               @NonNull HttpStatusCode httpStatusCode, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .errorMessage(ExceptionUtility.ARGUMENT_NOT_VALID)
                .errorTime(LocalDateTime.now())
                .methodArgErrors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
