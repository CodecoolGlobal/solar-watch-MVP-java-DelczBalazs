package com.codecool.solarwatch.weather;

import com.codecool.solarwatch.dto.WeatherDetailsDto;

import java.time.LocalDate;

public class DummyWeatherService implements WeatherService {
    @Override
    public WeatherDetailsDto getWeather(String city, LocalDate date) {
        double temperature = 21.8;
        double feelsLike = 21.0;
        int humidity = 58;
        double wind = 13.2;
        String windDir = "NE";
        int pressure = 1013;
        return new WeatherDetailsDto(temperature, feelsLike, humidity, wind, windDir, pressure);
    }
}
