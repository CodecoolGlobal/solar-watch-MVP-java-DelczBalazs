package com.codecool.solarwatch.client;

import com.codecool.solarwatch.model.openweather.OpenWeatherDirectItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class OpenWeatherClient {

    private final RestTemplate http;
    private final String base;
    private final String apiKey;

    public OpenWeatherClient(RestTemplate http,
                            @Value("${solarwatch.openweather.base-url}") String base,
                            @Value("${solarwatch.openweather.api-key}") String apiKey) {
        this.http = http;
        this.base = base;
        this.apiKey = apiKey;
    }

    public OpenWeatherDirectItemDto fetchFirstMatch(String q) {
        URI url = UriComponentsBuilder.fromUriString(base + "/geo/1.0/direct")
                .queryParam("q", q)
                .queryParam("limit", 1)
                .queryParam("appid", apiKey)
                .build()
                .encode()
                .toUri();

        OpenWeatherDirectItemDto[] items = http.getForObject(url, OpenWeatherDirectItemDto[].class);
        return (items != null && items.length > 0) ? items[0] : null;
    }
}
