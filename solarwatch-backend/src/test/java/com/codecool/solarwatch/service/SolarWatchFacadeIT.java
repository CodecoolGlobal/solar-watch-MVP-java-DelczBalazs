package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SunTimesResponseDto;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(SolarWatchFacade.class) // csak a facade bean
class SolarWatchFacadeIT {

    @Autowired
    private SolarWatchFacade facade;

    @MockBean
    private CityService cityService;

    @MockBean
    private SunTimesService sunTimesService;

    @Test
    void getSunTimes_returnsConvertedTimesAndCityMeta_endToEndThroughFacade() {
        City city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        when(cityService.findOrFetch("Budapest", "HU", null)).thenReturn(city);

        LocalDate date = LocalDate.of(2024, 6, 21);
        Instant sunriseUtc = LocalDateTime.of(2024, 6, 21, 3, 50).toInstant(ZoneOffset.UTC);
        Instant sunsetUtc  = LocalDateTime.of(2024, 6, 21, 19, 45).toInstant(ZoneOffset.UTC);
        SunTimes st = new SunTimes(city, date, sunriseUtc, sunsetUtc, (int) Duration.between(sunriseUtc, sunsetUtc).getSeconds());
        when(sunTimesService.findOrFetch(city, date, city.getLat(), city.getLon())).thenReturn(st);

        SunTimesResponseDto dto = facade.getSunTimes("Budapest", "HU", null, date, "Europe/Budapest");

        assertThat(dto.city()).isEqualTo("Budapest");
        assertThat(dto.country()).isEqualTo("HU");
        assertThat(dto.coordinates().lat()).isEqualTo(47.4979);
        assertThat(dto.coordinates().lon()).isEqualTo(19.0402);
        assertThat(dto.date()).isEqualTo(date);
        assertThat(dto.timezone()).isEqualTo("Europe/Budapest");
        assertThat(dto.sunrise().toInstant()).isEqualTo(sunriseUtc);
        assertThat(dto.sunset().toInstant()).isEqualTo(sunsetUtc);
    }

    @Test
    void getSunTimes_defaultsDateToTodayInRequestedZone_andUTCWhenNull() {
        City city = new City("London", "GB", null, 51.5074, -0.1278);
        when(cityService.findOrFetch("London", "GB", null)).thenReturn(city);

        ZoneId zone = ZoneOffset.UTC;
        LocalDate todayInUtc = LocalDate.now(zone);

        Instant rise = Instant.parse("2024-10-10T06:30:00Z");
        Instant set  = Instant.parse("2024-10-10T17:30:00Z");
        SunTimes st = new SunTimes(city, todayInUtc, rise, set, (int) Duration.between(rise, set).getSeconds());
        when(sunTimesService.findOrFetch(city, todayInUtc, city.getLat(), city.getLon())).thenReturn(st);

        SunTimesResponseDto dto = facade.getSunTimes("London", "GB", null, null, "UTC");

        assertThat(dto.date()).isEqualTo(todayInUtc);
        assertThat(dto.timezone()).isEqualTo("UTC");
        assertThat(dto.sunrise().toInstant()).isEqualTo(rise);
    }

    @Test
    void getSunTimes_trimsInputs_andThrowsOnInvalidTimezone() {
        City city = new City("Szeged", "HU", "", 46.253, 20.1414);
        when(cityService.findOrFetch("Szeged", "HU", "")).thenReturn(city);

        LocalDate date = LocalDate.of(2024, 3, 20);
        Instant rise = Instant.parse("2024-03-20T05:45:00Z");
        Instant set  = Instant.parse("2024-03-20T17:55:00Z");
        SunTimes st = new SunTimes(city, date, rise, set, (int) Duration.between(rise, set).getSeconds());
        when(sunTimesService.findOrFetch(city, date, city.getLat(), city.getLon())).thenReturn(st);

        SunTimesResponseDto ok = facade.getSunTimes("  Szeged ", " HU ", "  ", date, "UTC");
        assertThat(ok.city()).isEqualTo("Szeged");

        try {
            facade.getSunTimes("Szeged", "HU", null, date, "Europe/Invalid_City");
        } catch (RuntimeException e) {
            assertThat(e.getClass().getSimpleName()).containsIgnoringCase("InvalidTimezone");
        }
    }
}