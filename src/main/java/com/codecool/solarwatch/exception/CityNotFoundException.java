package com.codecool.solarwatch.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city) {
        super("No coordinates for city: " + city);
    }
}
