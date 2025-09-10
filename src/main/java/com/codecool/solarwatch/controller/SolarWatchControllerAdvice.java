package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class SolarWatchControllerAdvice {

    public record ApiError(Instant timestamp, int status, String error, String code, String message, String path) {}

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
}
