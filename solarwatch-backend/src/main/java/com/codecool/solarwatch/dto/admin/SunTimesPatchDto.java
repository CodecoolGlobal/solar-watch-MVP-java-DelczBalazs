package com.codecool.solarwatch.dto.admin;

import java.time.Instant;

public record SunTimesPatchDto(
        Instant sunriseUtc,
        Instant sunsetUtc,
        Integer dayLengthSec
) {}
