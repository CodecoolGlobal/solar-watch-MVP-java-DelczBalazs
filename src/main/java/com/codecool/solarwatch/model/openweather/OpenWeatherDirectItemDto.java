package com.codecool.solarwatch.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenWeatherDirectItemDto(
    String name, double lat, double lon, String country, String state
) {
}
