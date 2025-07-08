package com.study.url_shortener.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.study.url_shortener.models.WebResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().error(exception.getReason()).build());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> validationException(ConstraintViolationException exception) {
        return ResponseEntity.badRequest()
                .body(WebResponse.<String>builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<WebResponse<String>> noResourceFoundException(NoResourceFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(WebResponse.<String>builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> exception(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(WebResponse.<String>builder().error(exception.getMessage()).build());
    }
}
