package com.codecool.solarwatch.dto.openweather;

public record OpenWeatherCurrentResponseDto(
        Main main,
        Wind wind
) {
    public record Main(double temp, double feels_like, int pressure, int humidity) {}
    public record Wind(double speed, Integer deg) {}
}
