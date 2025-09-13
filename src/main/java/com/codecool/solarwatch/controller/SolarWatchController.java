package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunTimesResponseDto;
import com.codecool.solarwatch.service.SolarWatchFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/solarwatch")
public class SolarWatchController {

    private final SolarWatchFacade facade;

    public SolarWatchController(SolarWatchFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public SunTimesResponseDto getSunTimes(
            @RequestParam String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "UTC") String tz
    ) {
        return facade.getSunTimes(city, country, state, date, tz);
    }
}
