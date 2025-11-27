package com.codecool.solarwatch.dto;

public record WeatherDetailsDto(
        double temperatureCelsius,
        double feelsLikeCelsius,
        int uvIndex,
        int humidityPercent,
        double windSpeedKmh,
        String windDirection
) {}
