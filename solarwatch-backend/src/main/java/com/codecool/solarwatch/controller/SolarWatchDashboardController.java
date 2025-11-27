package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.SolarWatchDashboardDto;
import com.codecool.solarwatch.dto.SunTimesResponseDto;
import com.codecool.solarwatch.dto.WeatherDetailsDto;
import com.codecool.solarwatch.service.SolarWatchFacade;
import com.codecool.solarwatch.weather.WeatherService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping({"/api/solarwatch", "/api/solar-watch"})
public class SolarWatchDashboardController {

    private final SolarWatchFacade facade;
    private final WeatherService weatherService;

    public SolarWatchDashboardController(SolarWatchFacade facade, WeatherService weatherService) {
        this.facade = facade;
        this.weatherService = weatherService;
    }

    @GetMapping("/dashboard")
    public SolarWatchDashboardDto getDashboard(
            @RequestParam String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String c = city == null ? null : city.trim();
        if (c == null || c.isEmpty()) throw new IllegalArgumentException("city is required");

        SunTimesResponseDto base = facade.getSunTimes(c, null, null, date, "UTC");
        WeatherDetailsDto weather = weatherService.getWeather(base.city(), base.date());

        return new SolarWatchDashboardDto(
                base.city(),
                base.country(),
                base.state(),
                base.coordinates(),
                base.date(),
                base.timezone(),
                base.sunrise(),
                base.sunset(),
                weather
        );
    }
}
