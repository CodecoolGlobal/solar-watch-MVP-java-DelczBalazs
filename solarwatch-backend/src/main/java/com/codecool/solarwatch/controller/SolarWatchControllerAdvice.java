package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class SolarWatchControllerAdvice {

    // Consistent error payload
    public record ApiError(
            Instant timestamp, int status, String error, String code, String message, String path
    ) {}

    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError handleCity(HttpServletRequest req, CityNotFoundException ex) {
        return new ApiError(Instant.now(), 404, "City not found", "CITY_NOT_FOUND", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(InvalidTimezoneException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleTz(HttpServletRequest req, InvalidTimezoneException ex) {
        return new ApiError(Instant.now(), 400, "Invalid timezone", "INVALID_TIMEZONE", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleBadArgs(HttpServletRequest req, IllegalArgumentException ex) {
        return new ApiError(Instant.now(), 400, "Bad request", "INVALID_INPUT", ex.getMessage(), req.getRequestURI());
    }

    // Missing required query param (e.g., city)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleMissingParam(HttpServletRequest req, MissingServletRequestParameterException ex) {
        return new ApiError(Instant.now(), 400, "Bad request", "MISSING_PARAMETER", ex.getMessage(), req.getRequestURI());
    }

    // Wrong type / bad format (e.g., date not ISO yyyy-MM-dd)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleTypeMismatch(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        String raw = String.valueOf(ex.getValue());
        String msg = "Invalid value for parameter '" + ex.getName() + "': " + raw.strip();
        return new ApiError(Instant.now(), 400, "Bad request", "TYPE_MISMATCH", msg, req.getRequestURI());
    }

    // Upstream API failures (OpenWeather / sunrise-sunset)
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    ApiError handleUpstream(HttpServletRequest req, RestClientException ex) {
        return new ApiError(Instant.now(), 502, "Bad Gateway", "UPSTREAM_ERROR", "Failed to reach external API", req.getRequestURI());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleRse(ResponseStatusException ex,
                                            HttpServletRequest request) {
        var status = ex.getStatusCode();
        var body = Map.of(
                "timestamp", java.time.Instant.now().toString(),
                "status", status.value(),
                "error", status.toString(),
                "message", ex.getReason(),
                "path", request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    // Last-resort catch-all
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiError handleGeneric(HttpServletRequest req, Exception ex) {
        if (ex instanceof org.springframework.web.server.ResponseStatusException rse) {
            throw rse; // let the handler above format it
        }
        if (ex instanceof org.springframework.security.core.AuthenticationException) {
            throw (org.springframework.security.core.AuthenticationException) ex; // security handles as 401/403
        }
        return new ApiError(Instant.now(), 500, "Internal Server Error",
                "INTERNAL_ERROR", "Unexpected error occurred", req.getRequestURI());
    }
}
