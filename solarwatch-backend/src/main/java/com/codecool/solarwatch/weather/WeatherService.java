package com.codecool.solarwatch.weather;

import com.codecool.solarwatch.dto.WeatherDetailsDto;

import java.time.LocalDate;

public interface WeatherService {
    WeatherDetailsDto getWeather(String city, LocalDate date);
}
