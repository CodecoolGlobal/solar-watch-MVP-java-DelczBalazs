package com.codecool.solarwatch.dto.admin;

import java.time.Instant;
import java.time.LocalDate;

public record SunTimesAdminDto(
        Long id,
        Long cityId,
        LocalDate date,
        Instant sunriseUtc,
        Instant sunsetUtc,
        Integer dayLengthSec
) {}