package com.codecool.solarwatch.client;

import com.codecool.solarwatch.dto.openweather.OpenWeatherCurrentResponseDto;
import com.codecool.solarwatch.dto.openweather.OpenWeatherDirectItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class OpenWeatherClient {

    private final RestTemplate http;
    private final String base;
    private final String apiKey;

    public OpenWeatherClient(RestTemplate http, @Value("${solarwatch.openweather.base-url}") String base, @Value("${solarwatch.openweather.api-key}") String apiKey) {
        this.http = http;
        this.base = base;
        this.apiKey = apiKey;
    }

    public OpenWeatherDirectItemDto fetchFirstMatch(String q) {
        URI url = UriComponentsBuilder.fromUriString(base + "/geo/1.0/direct").queryParam("q", q).queryParam("limit", 1).queryParam("appid", apiKey).build().encode().toUri();

        try {
            OpenWeatherDirectItemDto[] items = http.getForObject(url, OpenWeatherDirectItemDto[].class);
            return (items != null && items.length > 0) ? items[0] : null;
        } catch (Exception e) {
            throw new RestClientException("OpenWeather geocoding failed", e);
        }
    }

    public OpenWeatherCurrentResponseDto fetchCurrentByCity(String city) {
        URI url = UriComponentsBuilder.fromUriString(base + "/data/2.5/weather").queryParam("q", city).queryParam("units", "metric").queryParam("appid", apiKey).build().encode().toUri();

        try {
            return http.getForObject(url, OpenWeatherCurrentResponseDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found for " + "weather");
        } catch (Exception e) {
            throw new RestClientException("OpenWeather current weather failed", e);
        }
    }
}
