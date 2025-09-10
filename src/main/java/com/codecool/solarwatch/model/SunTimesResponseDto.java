package com.codecool.solarwatch.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record SunTimesResponseDto(
        String city,
        String country,
        String state,
        Coordinates coordinates,
        LocalDate date,
        String timezone,
        OffsetDateTime sunrise,
        OffsetDateTime sunset,
        SourceMeta source
) {
    public record Coordinates(double lat, double lon) {}
    public record SourceMeta(String geocoding, String sun) {}
}
