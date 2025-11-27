package com.codecool.solarwatch.weather;

import com.codecool.solarwatch.dto.WeatherDetailsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DummyWeatherService implements WeatherService {
    @Override
    public WeatherDetailsDto getWeather(String city, LocalDate date) {
        // TODO: Replace with external weather API call (e.g., OpenWeatherMap current/forecast by city+date)
        // TODO: Optionally apply short-lived caching by city+date (e.g., @Cacheable)
        double temperature = 21.8;
        double feelsLike = 21.0;
        int uv = 4;
        int humidity = 58;
        double wind = 13.2;
        String windDir = "NE";
        return new WeatherDetailsDto(temperature, feelsLike, uv, humidity, wind, windDir);
    }
}
