package com.codecool.solarwatch.service;

import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import com.codecool.solarwatch.model.SunTimesResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarWatchFacadeTest {

    @Mock CityService cityService;
    @Mock SunTimesService sunTimesService;

    @InjectMocks SolarWatchFacade facade;

    @Test
    void convertsUtcAndLabelsTimezoneAsUTC_notZ() {
        // Given
        City city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        LocalDate day = LocalDate.of(2025, 8, 23);
        Instant sunriseUtc = Instant.parse("2025-08-23T03:00:00Z");
        Instant sunsetUtc  = Instant.parse("2025-08-23T18:00:00Z");
        SunTimes st = new SunTimes(city, day, sunriseUtc, sunsetUtc, 54000);

        when(cityService.findOrFetch("Budapest", "HU", null)).thenReturn(city);
        when(sunTimesService.findOrFetch(city, day, 47.4979, 19.0402)).thenReturn(st);

        // When
        SunTimesResponseDto resp = facade.getSunTimes("Budapest", "HU", null, day, "UTC");

        // Then
        assertThat(resp.timezone()).isEqualTo("UTC"); // if this fails, apply tzOut fix in Facade
        assertThat(resp.sunrise().toString()).isEqualTo("2025-08-23T03:00Z");
        assertThat(resp.sunset().toString()).isEqualTo("2025-08-23T18:00Z");
    }

    @Test
    void convertsToEuropeBudapest_correctOffsets() {
        // Given: 2025-08-23 is CEST (UTC+02)
        City city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        LocalDate day = LocalDate.of(2025, 8, 23);
        Instant sunriseUtc = Instant.parse("2025-08-23T03:00:00Z");
        Instant sunsetUtc  = Instant.parse("2025-08-23T18:00:00Z");
        SunTimes st = new SunTimes(city, day, sunriseUtc, sunsetUtc, 54000);

        when(cityService.findOrFetch("Budapest", "HU", null)).thenReturn(city);
        when(sunTimesService.findOrFetch(city, day, 47.4979, 19.0402)).thenReturn(st);

        // When
        SunTimesResponseDto resp = facade.getSunTimes("Budapest", "HU", null, day, "Europe/Budapest");

        // Then (03:00Z → 05:00+02, 18:00Z → 20:00+02)
        assertThat(resp.timezone()).isEqualTo("Europe/Budapest");
        assertThat(resp.sunrise().toString()).isEqualTo("2025-08-23T05:00+02:00");
        assertThat(resp.sunset().toString()).isEqualTo("2025-08-23T20:00+02:00");
    }

    @Test
    void throwsInvalidTimezone_forBadTz() {
        assertThatThrownBy(() -> facade.getSunTimes("Budapest", "HU", null, null, "Europe/Buda pest  \n"))
                .isInstanceOf(InvalidTimezoneException.class);
        // If you implemented trimming in parseZoneOrThrow, the message will be clean;
        // we don't assert the exact message here to avoid coupling.
    }

    @Test
    void usesTodayInRequestedZone_whenDateIsNull() {
        // Given
        ZoneId zone = ZoneId.of("Europe/Budapest");
        LocalDate todayInHu = LocalDate.now(zone); // Note: could be flaky around midnight in HU

        City city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        SunTimes st = new SunTimes(
                city,
                todayInHu,
                Instant.parse("2025-08-23T03:00:00Z"),
                Instant.parse("2025-08-23T18:00:00Z"),
                54000
        );

        when(cityService.findOrFetch("Budapest", "HU", null)).thenReturn(city);
        when(sunTimesService.findOrFetch(city, todayInHu, 47.4979, 19.0402)).thenReturn(st);

        // When
        SunTimesResponseDto resp = facade.getSunTimes("Budapest", "HU", null, null, "Europe/Budapest");

        // Then
        assertThat(resp.date()).isEqualTo(todayInHu);
        assertThat(resp.timezone()).isEqualTo("Europe/Budapest");
    }
}
