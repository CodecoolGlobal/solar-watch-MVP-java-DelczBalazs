package com.codecool.solarwatch.weather;

import com.codecool.solarwatch.client.OpenWeatherClient;
import com.codecool.solarwatch.dto.WeatherDetailsDto;
import com.codecool.solarwatch.dto.openweather.OpenWeatherCurrentResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OpenWeatherCurrentWeatherService implements WeatherService {

    private final OpenWeatherClient client;

    public OpenWeatherCurrentWeatherService(OpenWeatherClient client) {
        this.client = client;
    }

    @Override
    public WeatherDetailsDto getWeather(String city, LocalDate date) {
        OpenWeatherCurrentResponseDto resp = client.fetchCurrentByCity(city);
        if (resp == null || resp.main() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_GATEWAY,
                    "Weather provider returned empty response"
            );
        }
        double temp = resp.main().temp();
        double feels = resp.main().feels_like();
        int humidity = resp.main().humidity();
        int pressure = resp.main().pressure();
        double speedMps = resp.wind() != null ? resp.wind().speed() : 0.0;
        double speedKmh = speedMps * 3.6;
        String windDir = resp.wind() != null && resp.wind().deg() != null ? (resp.wind().deg() + "\u00B0") : "-";

        return new WeatherDetailsDto(temp, feels, humidity, speedKmh, windDir, pressure);
    }
}
