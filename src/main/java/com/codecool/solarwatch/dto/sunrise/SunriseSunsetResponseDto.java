package com.codecool.solarwatch.dto.sunrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetResponseDto(String status, ResultsDto results) {

    @JsonIgnoreProperties
    public record ResultsDto(String sunrise, String sunset) {}
}