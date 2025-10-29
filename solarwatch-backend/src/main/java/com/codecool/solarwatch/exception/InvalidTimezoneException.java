package com.codecool.solarwatch.exception;

public class InvalidTimezoneException extends RuntimeException {
    public InvalidTimezoneException(String tz) {
        super("Invalid timezone: " + tz);
    }
}
