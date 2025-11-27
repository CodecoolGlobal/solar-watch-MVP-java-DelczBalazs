package com.codecool.solarwatch.dto;

import com.codecool.solarwatch.dto.SunTimesResponseDto.Coordinates;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record SolarWatchDashboardDto(
        String city,
        String country,
        String state,
        Coordinates coordinates,
        LocalDate date,
        String timezone,
        OffsetDateTime sunrise,
        OffsetDateTime sunset,
        WeatherDetailsDto weather
) {}
