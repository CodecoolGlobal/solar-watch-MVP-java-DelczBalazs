package com.codecool.solarwatch.dto.admin;

// Boxed types so we can check nulls during PATCH
public record CityPatchDto(
        String name,
        String country,
        String state,
        Double lat,
        Double lon
) {}
