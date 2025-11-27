package com.codecool.solarwatch.dto;

public record WeatherDetailsDto(
        double temperatureCelsius,
        double feelsLikeCelsius,
        int humidityPercent,
        double windSpeedKmh,
        String windDirection,
        int pressureHpa
) {}
