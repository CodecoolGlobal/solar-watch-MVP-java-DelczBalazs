package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.SunriseSunsetClient;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.repository.SunTimesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SunTimesService {

    private final SunTimesRepository sunRepo;
    private final SunriseSunsetClient sunriseClient;

    public SunTimesService(SunTimesRepository sunRepo, SunriseSunsetClient sunriseClient) {
        this.sunRepo = sunRepo;
        this.sunriseClient = sunriseClient;
    }

    public SunTimes findOrFetch(City city, LocalDate date, double lat, double lon) {
        return sunRepo.findByCityAndDate(city, date).orElseGet(() -> {
            var utc = sunriseClient.fetchUtc(lat, lon, date);
            int dayLen = (int) ChronoUnit.SECONDS.between(utc.sunriseUtc(), utc.sunsetUtc());
            SunTimes saved = new SunTimes(city, date, utc.sunriseUtc(), utc.sunsetUtc(), dayLen);
            return sunRepo.save(saved);
        });
    }
}
