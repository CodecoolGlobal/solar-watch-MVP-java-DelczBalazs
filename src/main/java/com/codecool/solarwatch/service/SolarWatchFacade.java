package com.codecool.solarwatch.service;

import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import com.codecool.solarwatch.model.SunTimesResponseDto;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class SolarWatchFacade {

    private final CityService cityService;
    private final SunTimesService sunTimesService;

    public SolarWatchFacade(CityService cityService, SunTimesService sunTimesService) {
        this.cityService = cityService;
        this.sunTimesService = sunTimesService;
    }

    public SunTimesResponseDto getSunTimes(String city, String country, String state, LocalDate date, String tz) {
        // Default date to "today" in requested zone (or UTC if null)
        ZoneId zone = parseZoneOrThrow(tz);
        String tzOut = (zone.equals(ZoneOffset.UTC) ? "UTC" : zone.getId());
        LocalDate targetDate = (date != null) ? date : LocalDate.now(zone);

        City c = cityService.findOrFetch(safe(city), safe(country), safe(state));

        double plat = c.getLat();
        double plon = c.getLon();

        SunTimes st = sunTimesService.findOrFetch(c, targetDate, plat, plon);

        // Convert UTC instants to requested zone
        OffsetDateTime sunrise = st.getSunriseUtc().atOffset(ZoneOffset.UTC).atZoneSameInstant(zone).toOffsetDateTime();
        OffsetDateTime sunset  = st.getSunsetUtc().atOffset(ZoneOffset.UTC).atZoneSameInstant(zone).toOffsetDateTime();

        return new SunTimesResponseDto(
                c.getName(),
                c.getCountry(),
                c.getState(),
                new SunTimesResponseDto.Coordinates(plat, plon),
                targetDate,
                tzOut,
                sunrise,
                sunset,
                new SunTimesResponseDto.SourceMeta("db+openweather", "db+sunrise-sunset")
        );
    }

    private ZoneId parseZoneOrThrow(String tz) {
        if (tz == null || tz.isBlank() || "UTC".equalsIgnoreCase(tz)) return ZoneOffset.UTC;
        try { return ZoneId.of(tz.trim()); }
        catch (Exception e) { throw new InvalidTimezoneException(tz.trim()); }
    }

    private String safe(String s) { return s == null ? null : s.strip(); }
}
