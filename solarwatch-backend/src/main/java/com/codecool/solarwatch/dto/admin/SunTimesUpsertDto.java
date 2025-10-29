package com.codecool.solarwatch.dto.admin;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record SunTimesUpsertDto(
        @NotNull Instant sunriseUtc,
        @NotNull Instant sunsetUtc,
        @NotNull Integer dayLengthSec
) {}
