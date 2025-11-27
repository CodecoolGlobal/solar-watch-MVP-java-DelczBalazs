package com.codecool.solarwatch.weather;

import com.codecool.solarwatch.dto.WeatherDetailsDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DummyWeatherServiceTest {

    @Test
    void returnsRealisticDummyValues() {
        WeatherService svc = new DummyWeatherService();
        WeatherDetailsDto dto = svc.getWeather("Budapest", LocalDate.of(2025, 1, 1));

        assertThat(dto.temperatureCelsius()).isBetween(-50.0, 60.0);
        assertThat(dto.feelsLikeCelsius()).isBetween(-50.0, 60.0);
        assertThat(dto.uvIndex()).isBetween(0, 12);
        assertThat(dto.humidityPercent()).isBetween(0, 100);
        assertThat(dto.windSpeedKmh()).isBetween(0.0, 200.0);
        assertThat(dto.windDirection()).isNotBlank();
    }
}
