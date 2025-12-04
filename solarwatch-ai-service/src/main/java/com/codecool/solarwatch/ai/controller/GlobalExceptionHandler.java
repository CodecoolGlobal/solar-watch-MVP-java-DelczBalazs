package com.codecool.solarwatch.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    Map<String, String> violations = ex.getBindingResult().getFieldErrors().stream()
      .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a, b) -> a, LinkedHashMap::new));
    return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), violations);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
    Map<String, String> violations = ex.getConstraintViolations().stream()
      .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage(), (a, b) -> a, LinkedHashMap::new));
    return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), violations);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", req.getRequestURI(), null);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
    log.error("Unhandled exception at {}", req.getRequestURI(), ex);
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", req.getRequestURI(), null);
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String message, String path, Map<String, String> violations) {
    ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path, violations);
    return ResponseEntity.status(status).body(body);
  }

  public record ApiError(Instant timestamp, int status, String error, String message, String path, Map<String, String> violations) {}
}
