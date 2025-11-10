package com.codecool.solarwatch.dto.admin;

public record CityAdminDto(
        Long id,
        String name,
        String country,
        String state,
        double lat,
        double lon
) {}