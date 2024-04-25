package org.practise.crud.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.practise.crud.Response.ErrorResponse;
import org.practise.crud.Response.FailResponse;
import org.practise.crud.Response.Response;
import org.practise.crud.utils.ElementAlreadyPresentException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Response> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex){
        log.warn("MethodArgumentException occurred: {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(ex.getBindingResult().toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolation(
            ConstraintViolationException ex)
    {
        log.warn("ConstraintViolationException occurred: {}", ex.getMessage());
        Set<String> violations =
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new FailResponse<>(violations));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Response> handleIllegalAccessException(
            IllegalAccessException ex
    ){
        log.warn("IllegalAccessException occurred: {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response> handleNoSuchElementException(
            NoSuchElementException ex
    ){
        log.warn("NoSuchElementException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(new FailResponse<>(ex.getMessage()));
    }
    @ExceptionHandler(ElementAlreadyPresentException.class)
    public ResponseEntity<Response> handleElementAlreadyPresentException(
            ElementAlreadyPresentException ex
    ){
        log.warn("ElementAlreadyPresentException: {}", ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(new FailResponse<>(ex.getMessage()));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDenied(AccessDeniedException ex) {
        log.warn("AccessDeniedException occurred: {}", ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(new FailResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response> handleException(Exception ex) {
        log.error("An exception occurred", ex);
        String message = ex.getMessage() == null ? ex.toString() : ex + " " + ex.getMessage();
        message = message + "\n" + Arrays.toString(ex.getStackTrace());
        message = message.length() > 1000 ? message.substring(0, 1000) : message;
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
    }
}
