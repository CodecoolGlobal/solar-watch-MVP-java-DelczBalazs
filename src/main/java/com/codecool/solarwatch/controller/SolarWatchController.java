package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunTimesResponseDto;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/solarwatch")
public class SolarWatchController {

    private final SolarWatchService service;

    public SolarWatchController(SolarWatchService service) {
        this.service = service;
    }

    @GetMapping
    public SunTimesResponseDto getSunTimes(
        @RequestParam String city,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String state,
        @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "UTC") String tz
    ) {
        return service.getSunTimes(city, country, state, date, tz);
    }
}
