package com.codecool.solarwatch.client;

import com.codecool.solarwatch.dto.sunrise.SunriseSunsetResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.*;

@Component
public class SunriseSunsetClient {

    private final RestTemplate http;
    private final String base;

    public SunriseSunsetClient(RestTemplate http,
                            @Value("${solarwatch.sunrise.base-url}") String base) {
        this.http = http;
        this.base = base;
    }

    /** Returns UTC instants for sunrise/sunset for given lat/lon/date. */
    public UtcTimes fetchUtc(double lat, double lon, LocalDate date) {
        URI url = UriComponentsBuilder.fromUriString(base + "/json")
                .queryParam("lat", lat)
                .queryParam("lng", lon)
                .queryParam("date", date)
                .queryParam("formatted", 0) // ISO 8601 with UTC offset
                .build()
                .encode()
                .toUri();

        SunriseSunsetResponseDto resp = http.getForObject(url, SunriseSunsetResponseDto.class);
        if (resp == null || resp.results() == null) {
            throw new IllegalStateException("Sunrise API returned empty result");
        }

        ZonedDateTime sunriseUtc = OffsetDateTime.parse(resp.results().sunrise()).atZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime sunsetUtc  = OffsetDateTime.parse(resp.results().sunset()).atZoneSameInstant(ZoneOffset.UTC);

        return new UtcTimes(sunriseUtc.toInstant(), sunsetUtc.toInstant());
    }

    public record UtcTimes(Instant sunriseUtc, Instant sunsetUtc) {}
}
