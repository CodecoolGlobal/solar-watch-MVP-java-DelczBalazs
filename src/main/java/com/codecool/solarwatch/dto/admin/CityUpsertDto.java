package com.codecool.solarwatch.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CityUpsertDto(
        @NotBlank String name,
        @NotBlank String country,
        String state,
        @NotNull double lat,
        @NotNull double lon
) {}
