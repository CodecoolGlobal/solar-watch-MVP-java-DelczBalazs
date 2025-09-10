package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import com.codecool.solarwatch.model.SunTimesResponseDto;
import com.codecool.solarwatch.model.openweather.OpenWeatherDirectItemDto;
import com.codecool.solarwatch.model.sunrise.SunriseSunsetResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.*;

@Service
public class SolarWatchService {

    private final RestTemplate http;
    private final String owBase;
    private final String owKey;
    private final String sunBase;

    public SolarWatchService(
            RestTemplate http,
            @Value("${solarwatch.openweather.base-url}") String owBase,
            @Value("${solarwatch.openweather.api-key}") String owKey,
            @Value("${solarwatch.sunrise.base-url}") String sunBase
    ) {
        this.http = http;
        this.owBase = owBase;
        this.owKey = owKey;
        this.sunBase = sunBase;
    }

    // ===== PUBLIC API the controller calls =====
    public SunTimesResponseDto getSunTimes(
            String city,
            String country,
            String state,
            LocalDate date,
            String tz
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now(ZoneOffset.UTC);
        ZoneId zone = parseZoneOrThrow(tz);

        Coords coords = geocodeOrThrow(city, country, state);               // (1) city -> lat/lon
        UtcTimes utc   = fetchSunriseSunsetUtc(coords.lat(), coords.lon(), targetDate); // (2) lat/lon/date -> sunrise/sunset (UTC)
        String timezoneLabel = (zone instanceof java.time.ZoneOffset) ? "UTC" : zone.getId();

        // (3) convert to requested timezone and build our response DTO
        return new SunTimesResponseDto(
                city, country, state,
                new SunTimesResponseDto.Coordinates(coords.lat(), coords.lon()),
                targetDate,
                timezoneLabel,
                utc.sunrise().withZoneSameInstant(zone).toOffsetDateTime(),
                utc.sunset().withZoneSameInstant(zone).toOffsetDateTime(),
                new SunTimesResponseDto.SourceMeta("openweathermap", "sunrise-sunset.org")
        );
    }

    // ===== helpers (private) =====

    private record Coords(double lat, double lon) {}
    private record UtcTimes(ZonedDateTime sunrise, ZonedDateTime sunset) {}

    // A) geocoding: OpenWeather "direct" endpoint: q="city[,state][,country]"
    private Coords geocodeOrThrow(String city, String country, String state) {
        if (city == null || city.isBlank()) throw new IllegalArgumentException("city is required");

        String q = city.trim()
                + (state != null && !state.isBlank() ? "," + state.trim() : "")
                + (country != null && !country.isBlank() ? "," + country.trim() : "");

        URI url = UriComponentsBuilder.fromHttpUrl(owBase + "/geo/1.0/direct")
                .queryParam("q", q)
                .queryParam("limit", 1)
                .queryParam("appid", owKey)
                .build(true)
                .toUri();

        OpenWeatherDirectItemDto[] items = http.getForObject(url, OpenWeatherDirectItemDto[].class);
        if (items == null || items.length == 0) throw new CityNotFoundException(city);
        return new Coords(items[0].lat(), items[0].lon());
    }

    // B) sunrise-sunset: returns UTC strings inside "results"
    private UtcTimes fetchSunriseSunsetUtc(double lat, double lon, LocalDate date) {
        URI url = UriComponentsBuilder.fromHttpUrl(sunBase + "/json")
                .queryParam("lat", lat)
                .queryParam("lng", lon)
                .queryParam("date", date)
                .queryParam("formatted", 0) // ISO-8601 UTC
                .build()
                .toUri();

        SunriseSunsetResponseDto resp = http.getForObject(url, SunriseSunsetResponseDto.class);
        if (resp == null || resp.results() == null) {
            throw new IllegalStateException("Invalid response from sunrise-sunset");
        }

        ZonedDateTime sunriseUtc = OffsetDateTime.parse(resp.results().sunrise())
                .atZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime sunsetUtc  = OffsetDateTime.parse(resp.results().sunset())
                .atZoneSameInstant(ZoneOffset.UTC);

        return new UtcTimes(sunriseUtc, sunsetUtc);
    }

    // C) timezone parsing: default UTC, or validate IANA id (e.g. "Europe/Budapest")
    private ZoneId parseZoneOrThrow(String tz) {
        if (tz == null || tz.isBlank() || "UTC".equalsIgnoreCase(tz)) return ZoneOffset.UTC;
        try { return ZoneId.of(tz); }
        catch (Exception e) { throw new InvalidTimezoneException(tz); }
    }
}
