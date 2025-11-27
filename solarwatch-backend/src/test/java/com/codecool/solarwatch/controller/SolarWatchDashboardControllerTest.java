package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SolarWatchDashboardDto;
import com.codecool.solarwatch.dto.SunTimesResponseDto;
import com.codecool.solarwatch.dto.WeatherDetailsDto;
import com.codecool.solarwatch.service.SolarWatchFacade;
import com.codecool.solarwatch.weather.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarWatchDashboardControllerTest {

    @Mock
    SolarWatchFacade facade;

    @Mock
    WeatherService weatherService;

    @InjectMocks
    SolarWatchDashboardController controller;

    @Test
    void aggregatesSolarAndWeather() {
        LocalDate date = LocalDate.of(2025, 8, 23);
        SunTimesResponseDto base = new SunTimesResponseDto(
                "Budapest",
                "HU",
                null,
                new SunTimesResponseDto.Coordinates(47.4979, 19.0402),
                date,
                "UTC",
                OffsetDateTime.parse("2025-08-23T03:00:00Z"),
                OffsetDateTime.parse("2025-08-23T18:00:00Z"),
                new SunTimesResponseDto.SourceMeta("db+openweather", "db+sunrise-sunset")
        );
        WeatherDetailsDto weather = new WeatherDetailsDto(22.0, 21.0, 4, 60, 12.3, "NE");

        when(facade.getSunTimes("Budapest", null, null, date, "UTC")).thenReturn(base);
        when(weatherService.getWeather("Budapest", date)).thenReturn(weather);

        SolarWatchDashboardDto dto = controller.getDashboard("Budapest", date);

        assertThat(dto.city()).isEqualTo("Budapest");
        assertThat(dto.sunrise()).isEqualTo(base.sunrise());
        assertThat(dto.weather().uvIndex()).isEqualTo(4);

        verify(weatherService).getWeather("Budapest", date);
    }
}
